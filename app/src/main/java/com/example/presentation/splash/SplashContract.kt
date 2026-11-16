package com.example.presentation.splash

import com.example.core.mvi.MviIntent
import com.example.core.mvi.MviSideEffect
import com.example.core.mvi.MviState

data class SplashState(
    val isLoading: Boolean = true,
    val isInitializationComplete: Boolean = false,
    val isAnimationComplete: Boolean = false,
    val errorMessage: String? = null
) : MviState

sealed interface SplashIntent : MviIntent {
    data object Initialize : SplashIntent
    data object AnimationFinished : SplashIntent
    data object Retry : SplashIntent
}

sealed interface SplashEffect : MviSideEffect {
    data object NavigateToHome : SplashEffect
    data object NavigateToLogin : SplashEffect
}
