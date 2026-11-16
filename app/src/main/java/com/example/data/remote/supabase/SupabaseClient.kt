package com.example.data.remote.supabase

import com.example.core.result.AppResult
import com.example.core.result.NetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

/**
 * Lightweight, robust Supabase Client using standard PostgREST and GoTrue protocols.
 * Supports CRUD on any Supabase table via REST v1 endpoints.
 */
class SupabaseClient(
    private val okHttpClient: OkHttpClient,
    private val json: Json,
    private var config: SupabaseConfig
) {
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    fun updateConfig(newConfig: SupabaseConfig) {
        this.config = newConfig
    }

    fun isConfigured(): Boolean = config.isConfigured

    fun from(table: String): TableQueryBuilder = TableQueryBuilder(table)

    inner class TableQueryBuilder(private val table: String) {

        /**
         * PostgREST SELECT query: GET /rest/v1/{table}?select=*&{filters}
         */
        suspend fun select(
            selectColumns: String = "*",
            filterQuery: String? = null,
            order: String = "created_at.desc",
            limit: Int = 50
        ): AppResult<String> = withContext(Dispatchers.IO) {
            val baseUrl = sanitizeUrl(config.url)
            val queryParams = buildList {
                add("select=$selectColumns")
                if (!filterQuery.isNullOrBlank()) add(filterQuery)
                if (order.isNotBlank()) add("order=$order")
                if (limit > 0) add("limit=$limit")
            }.joinToString("&")

            val fullUrl = "$baseUrl/rest/v1/$table?$queryParams"
            val request = Request.Builder()
                .url(fullUrl)
                .addHeader("apikey", config.anonKey)
                .addHeader("Authorization", "Bearer ${config.authToken ?: config.anonKey}")
                .addHeader("Accept", "application/json")
                .get()
                .build()

            executeRequest(request)
        }

        /**
         * PostgREST INSERT query: POST /rest/v1/{table}
         * Header Prefer: return=representation ensures newly created rows are returned.
         */
        suspend fun insert(jsonBody: String): AppResult<String> = withContext(Dispatchers.IO) {
            val baseUrl = sanitizeUrl(config.url)
            val fullUrl = "$baseUrl/rest/v1/$table"
            val requestBody = jsonBody.toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url(fullUrl)
                .addHeader("apikey", config.anonKey)
                .addHeader("Authorization", "Bearer ${config.authToken ?: config.anonKey}")
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=representation")
                .post(requestBody)
                .build()

            executeRequest(request)
        }

        /**
         * PostgREST DELETE query: DELETE /rest/v1/{table}?{filter}
         */
        suspend fun delete(column: String, value: String): AppResult<String> = withContext(Dispatchers.IO) {
            val baseUrl = sanitizeUrl(config.url)
            val fullUrl = "$baseUrl/rest/v1/$table?$column=eq.$value"

            val request = Request.Builder()
                .url(fullUrl)
                .addHeader("apikey", config.anonKey)
                .addHeader("Authorization", "Bearer ${config.authToken ?: config.anonKey}")
                .delete()
                .build()

            executeRequest(request)
        }

        /**
         * PostgREST UPDATE query: PATCH /rest/v1/{table}?{filter}
         */
        suspend fun update(column: String, value: String, jsonBody: String): AppResult<String> = withContext(Dispatchers.IO) {
            val baseUrl = sanitizeUrl(config.url)
            val fullUrl = "$baseUrl/rest/v1/$table?$column=eq.$value"
            val requestBody = jsonBody.toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url(fullUrl)
                .addHeader("apikey", config.anonKey)
                .addHeader("Authorization", "Bearer ${config.authToken ?: config.anonKey}")
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=representation")
                .patch(requestBody)
                .build()

            executeRequest(request)
        }
    }

    private fun executeRequest(request: Request): AppResult<String> {
        return try {
            val response = okHttpClient.newCall(request).execute()
            val body = response.body?.string() ?: ""
            if (response.isSuccessful) {
                AppResult.Success(body)
            } else {
                when (response.code) {
                    401, 403 -> AppResult.Error(NetworkError.Unauthorized("HTTP ${response.code}: $body"))
                    404 -> AppResult.Error(NetworkError.NotFound("Endpoint not found: ${request.url}"))
                    in 500..599 -> AppResult.Error(NetworkError.ServerError(response.code, body))
                    else -> AppResult.Error(NetworkError.Unknown("HTTP ${response.code}: $body"))
                }
            }
        } catch (e: IOException) {
            AppResult.Error(NetworkError.NoConnectivity(e.message ?: "Failed to connect to Supabase"))
        } catch (e: Exception) {
            AppResult.Error(NetworkError.Unknown(e.message ?: "Unexpected error", e))
        }
    }

    private fun sanitizeUrl(url: String): String {
        return url.trim().trimEnd('/')
    }
}
