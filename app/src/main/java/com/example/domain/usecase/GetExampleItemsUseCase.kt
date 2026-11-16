package com.example.domain.usecase

import com.example.core.result.AppResult
import com.example.domain.model.ExampleItem
import com.example.domain.repository.ExampleRepository
import kotlinx.coroutines.flow.Flow

class GetExampleItemsUseCase(
    private val repository: ExampleRepository
) {
    operator fun invoke(): Flow<AppResult<List<ExampleItem>>> {
        return repository.getItemsStream()
    }
}
