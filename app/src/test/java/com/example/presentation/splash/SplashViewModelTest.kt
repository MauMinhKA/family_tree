package com.example.presentation.splash

import androidx.test.core.app.ApplicationProvider
import com.example.ArchBaseApplication
import com.example.domain.usecase.AuthDestination
import com.example.domain.usecase.CheckAuthStatusUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when animation finishes and init complete, NavigateToHome is sent`() = runTest(testDispatcher) {
        val app = ApplicationProvider.getApplicationContext<ArchBaseApplication>()
        val useCase = app.container.checkAuthStatusUseCase

        val viewModel = SplashViewModel(useCase)

        // Advance coroutines for initialization
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isInitializationComplete)

        // Trigger animation finished
        viewModel.processIntent(SplashIntent.AnimationFinished)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isAnimationComplete)
        val effect = viewModel.sideEffect.first()
        assertEquals(SplashEffect.NavigateToHome, effect)
    }

    @Test
    fun `check auth status returns LOGIN when required and token missing`() = runTest(testDispatcher) {
        val app = ApplicationProvider.getApplicationContext<ArchBaseApplication>()
        val useCase = app.container.checkAuthStatusUseCase

        val destination = useCase(isAuthRequired = true)
        assertEquals(AuthDestination.LOGIN, destination)
    }

    @Test
    fun `check auth status returns HOME when auth not required`() = runTest(testDispatcher) {
        val app = ApplicationProvider.getApplicationContext<ArchBaseApplication>()
        val useCase = app.container.checkAuthStatusUseCase

        val destination = useCase(isAuthRequired = false)
        assertEquals(AuthDestination.HOME, destination)
    }
}
