package com.example.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ArchBaseApplication
import com.example.presentation.home.HomeScreen
import com.example.presentation.home.HomeViewModel
import com.example.presentation.login.LoginScreen
import com.example.presentation.splash.SplashScreen
import com.example.presentation.splash.SplashViewModel

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val app = LocalContext.current.applicationContext as ArchBaseApplication
    val container = app.container

    NavHost(
        navController = navController,
        startDestination = ScreenRoute.Splash,
        modifier = modifier.fillMaxSize()
    ) {
        composable<ScreenRoute.Splash> {
            val splashViewModel: SplashViewModel = viewModel {
                SplashViewModel(
                    checkAuthStatusUseCase = container.checkAuthStatusUseCase
                )
            }
            SplashScreen(
                viewModel = splashViewModel,
                onNavigateToHome = {
                    navController.navigate(ScreenRoute.Home) {
                        popUpTo(ScreenRoute.Splash) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(ScreenRoute.Login) {
                        popUpTo(ScreenRoute.Splash) { inclusive = true }
                    }
                }
            )
        }

        composable<ScreenRoute.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(ScreenRoute.Home) {
                        popUpTo(ScreenRoute.Login) { inclusive = true }
                    }
                }
            )
        }

        composable<ScreenRoute.Home> {
            val homeViewModel: HomeViewModel = viewModel {
                HomeViewModel(
                    getExampleItemsUseCase = container.getExampleItemsUseCase,
                    preferencesDataSource = container.preferencesDataSource
                )
            }
            HomeScreen(viewModel = homeViewModel)
        }
    }
}
