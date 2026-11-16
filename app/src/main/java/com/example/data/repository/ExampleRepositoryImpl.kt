package com.example.data.repository

import com.example.core.result.AppResult
import com.example.data.local.datastore.AppPreferencesDataSource
import com.example.data.remote.supabase.SupabaseClient
import com.example.domain.model.ExampleItem
import com.example.domain.repository.ExampleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class ExampleRepositoryImpl(
    private val supabaseClient: SupabaseClient,
    private val preferencesDataSource: AppPreferencesDataSource,
    private val json: Json
) : ExampleRepository {

    private val localItems = MutableStateFlow<List<ExampleItem>>(
        listOf(
            ExampleItem(
                id = "1",
                title = "Clean Architecture + MVI",
                description = "Separation of Domain, Data, Presentation layers with unidirectional data flow"
            ),
            ExampleItem(
                id = "2",
                title = "Supabase & DataStore Ready",
                description = "Network client configured for Supabase PostgREST and Preferences DataStore"
            )
        )
    )

    override fun getItemsStream(): Flow<AppResult<List<ExampleItem>>> {
        return localItems.asStateFlow().map { items ->
            AppResult.Success(items)
        }
    }

    override suspend fun addItem(item: ExampleItem): AppResult<ExampleItem> {
        localItems.value = localItems.value + item
        return AppResult.Success(item)
    }
}
