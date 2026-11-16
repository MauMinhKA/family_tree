package com.example.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel for MVI (Model-View-Intent) Architecture.
 *
 * Exposes:
 * - [uiState]: [StateFlow] representing current persistent screen state.
 * - [sideEffect]: [receiveAsFlow] for one-time events (snackbars, navigation).
 * - [processIntent]: Entry point for user actions coming from Composables.
 */
abstract class MviViewModel<State : MviState, Intent : MviIntent, Effect : MviSideEffect>(
    initialState: State
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val _sideEffect = Channel<Effect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    protected val currentState: State
        get() = _uiState.value

    /**
     * Dispatch an intent into the ViewModel.
     */
    abstract fun processIntent(intent: Intent)

    /**
     * Atomically mutate the current state.
     */
    protected fun setState(reducer: State.() -> State) {
        _uiState.value = _uiState.value.reducer()
    }

    /**
     * Emit a one-time side effect to the UI.
     */
    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}
