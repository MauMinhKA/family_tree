package com.example.presentation.splash

import androidx.lifecycle.viewModelScope
import com.example.core.mvi.MviViewModel
import com.example.domain.usecase.AuthDestination
import com.example.domain.usecase.CheckAuthStatusUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class SplashViewModel(
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase
) : MviViewModel<SplashState, SplashIntent, SplashEffect>(SplashState()) {

    private var targetDestination: AuthDestination = AuthDestination.HOME
    private val hasDispatchedNavigation = AtomicBoolean(false)

    init {
        processIntent(SplashIntent.Initialize)
    }

    override fun processIntent(intent: SplashIntent) {
        when (intent) {
            SplashIntent.Initialize -> initializeApp()
            SplashIntent.AnimationFinished -> onAnimationFinished()
            SplashIntent.Retry -> initializeApp()
        }
    }

    private fun initializeApp() {
        setState { copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                // Determine destination based on authentication/session status
                targetDestination = checkAuthStatusUseCase()

                setState { copy(isInitializationComplete = true, isLoading = false) }

                // If animation has already completed, dispatch navigation immediately
                if (currentState.isAnimationComplete) {
                    dispatchNavigation()
                }
            } catch (e: Exception) {
                // In case of any unexpected init error, fallback safely to Home after delay
                setState {
                    copy(
                        isLoading = false,
                        isInitializationComplete = true,
                        errorMessage = e.localizedMessage
                    )
                }
                if (currentState.isAnimationComplete) {
                    dispatchNavigation()
                }
            }
        }

        // Safety fallback timer: navigate after 3.5 seconds regardless of animation state
        viewModelScope.launch {
            delay(3500)
            dispatchNavigation()
        }
    }

    private fun onAnimationFinished() {
        setState { copy(isAnimationComplete = true) }
        if (currentState.isInitializationComplete) {
            dispatchNavigation()
        }
    }

    private fun dispatchNavigation() {
        if (hasDispatchedNavigation.compareAndSet(false, true)) {
            val effect = when (targetDestination) {
                AuthDestination.HOME -> SplashEffect.NavigateToHome
                AuthDestination.LOGIN -> SplashEffect.NavigateToLogin
            }
            sendEffect(effect)
        }
    }
}
