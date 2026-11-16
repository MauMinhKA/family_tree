package com.example.core.result

sealed class NetworkError(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class NoConnectivity(message: String = "No internet connection available") : NetworkError(message)
    class Unauthorized(message: String = "Supabase API key or auth token is invalid or expired") : NetworkError(message)
    class NotFound(message: String = "Resource not found on Supabase endpoint") : NetworkError(message)
    class ServerError(val statusCode: Int, message: String = "Supabase server error (HTTP $statusCode)") : NetworkError(message)
    class SerializationError(message: String, cause: Throwable? = null) : NetworkError(message, cause)
    class Unknown(message: String, cause: Throwable? = null) : NetworkError(message, cause)
}
