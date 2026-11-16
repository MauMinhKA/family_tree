package com.example.presentation.home

import androidx.lifecycle.viewModelScope
import com.example.core.mvi.MviViewModel
import com.example.core.result.AppResult
import com.example.data.local.datastore.AppPreferencesDataSource
import com.example.domain.usecase.GetExampleItemsUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getExampleItemsUseCase: GetExampleItemsUseCase,
    private val preferencesDataSource: AppPreferencesDataSource
) : MviViewModel<HomeState, HomeIntent, HomeEffect>(HomeState(isLoading = true)) {

    init {
        observeTheme()
        processIntent(HomeIntent.LoadData)
    }

    override fun processIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadData -> loadData()
            is HomeIntent.ToggleTheme -> toggleTheme(intent.isDark)
        }
    }

    private fun loadData() {
        getExampleItemsUseCase().onEach { result ->
            when (result) {
                is AppResult.Success -> setState { copy(items = result.data, isLoading = false) }
                is AppResult.Error -> {
                    setState { copy(isLoading = false) }
                    sendEffect(HomeEffect.ShowMessage(result.message))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun observeTheme() {
        preferencesDataSource.isDarkThemeFlow.onEach { isDark ->
            setState { copy(isDarkTheme = isDark) }
        }.launchIn(viewModelScope)
    }

    private fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            preferencesDataSource.setDarkTheme(isDark)
        }
    }
}
