package com.example.domain.usecase

import com.example.data.local.datastore.AppPreferencesDataSource
import kotlinx.coroutines.flow.firstOrNull

enum class AuthDestination {
    HOME,
    LOGIN
}

/**
 * UseCase to determine the start destination after Splash initialization.
 * Evaluates whether authentication is required and whether a valid session token exists.
 */
class CheckAuthStatusUseCase(
    private val preferencesDataSource: AppPreferencesDataSource
) {
    suspend operator fun invoke(isAuthRequired: Boolean = false): AuthDestination {
        val userToken = preferencesDataSource.userTokenFlow.firstOrNull()
        return if (isAuthRequired && userToken.isNullOrBlank()) {
            AuthDestination.LOGIN
        } else {
            AuthDestination.HOME
        }
    }
}
