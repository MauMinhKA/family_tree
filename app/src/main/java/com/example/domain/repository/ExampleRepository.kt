package com.example.domain.repository

import com.example.core.result.AppResult
import com.example.domain.model.ExampleItem
import kotlinx.coroutines.flow.Flow

interface ExampleRepository {
    fun getItemsStream(): Flow<AppResult<List<ExampleItem>>>
    suspend fun addItem(item: ExampleItem): AppResult<ExampleItem>
}
