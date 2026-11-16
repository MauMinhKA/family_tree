package com.example.core.di

import android.content.Context
import com.example.data.local.datastore.AppPreferencesDataSource
import com.example.data.local.datastore.dataStore
import com.example.data.remote.supabase.SupabaseClient
import com.example.data.remote.supabase.SupabaseConfig
import com.example.data.repository.ExampleRepositoryImpl
import com.example.domain.repository.ExampleRepository
import com.example.domain.usecase.CheckAuthStatusUseCase
import com.example.domain.usecase.GetExampleItemsUseCase
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Dependency Injection Container for Clean Architecture base project.
 * Provides application singletons and use cases.
 */
interface AppContainer {
    val exampleRepository: ExampleRepository
    val preferencesDataSource: AppPreferencesDataSource
    val getExampleItemsUseCase: GetExampleItemsUseCase
    val checkAuthStatusUseCase: CheckAuthStatusUseCase
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    // Network & Serialization Module
    val json: Json by lazy {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        }
    }

    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    val supabaseClient: SupabaseClient by lazy {
        SupabaseClient(
            okHttpClient = okHttpClient,
            json = json,
            config = SupabaseConfig()
        )
    }

    // Local DataStore Module
    override val preferencesDataSource: AppPreferencesDataSource by lazy {
        AppPreferencesDataSource(context.dataStore)
    }

    // Repository Module
    override val exampleRepository: ExampleRepository by lazy {
        ExampleRepositoryImpl(
            supabaseClient = supabaseClient,
            preferencesDataSource = preferencesDataSource,
            json = json
        )
    }

    // UseCase Module
    override val getExampleItemsUseCase: GetExampleItemsUseCase by lazy {
        GetExampleItemsUseCase(exampleRepository)
    }

    override val checkAuthStatusUseCase: CheckAuthStatusUseCase by lazy {
        CheckAuthStatusUseCase(preferencesDataSource)
    }
}
