package com.example.presentation.home

import com.example.core.mvi.MviIntent
import com.example.core.mvi.MviSideEffect
import com.example.core.mvi.MviState
import com.example.domain.model.ExampleItem

data class HomeState(
    val isLoading: Boolean = false,
    val items: List<ExampleItem> = emptyList(),
    val isDarkTheme: Boolean = false
) : MviState

sealed interface HomeIntent : MviIntent {
    data object LoadData : HomeIntent
    data class ToggleTheme(val isDark: Boolean) : HomeIntent
}

sealed interface HomeEffect : MviSideEffect {
    data class ShowMessage(val message: String) : HomeEffect
}
