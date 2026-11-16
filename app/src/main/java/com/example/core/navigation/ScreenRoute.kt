package com.example.core.navigation

import kotlinx.serialization.Serializable

sealed interface ScreenRoute {
    @Serializable
    data object Splash : ScreenRoute

    @Serializable
    data object Home : ScreenRoute

    @Serializable
    data object Login : ScreenRoute
}
