package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.core.navigation.AppNavHost
import com.example.ui.theme.ArchBaseTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as ArchBaseApplication
        val isDarkThemeFlow = app.container.preferencesDataSource.isDarkThemeFlow

        setContent {
            val systemDark = isSystemInDarkTheme()
            val isDark by isDarkThemeFlow.collectAsStateWithLifecycle(initialValue = systemDark)

            ArchBaseTheme(darkTheme = isDark) {
                AppNavHost()
            }
        }
    }
}
