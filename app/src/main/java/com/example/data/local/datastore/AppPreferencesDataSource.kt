package com.example.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class AppPreferencesDataSource(private val dataStore: DataStore<Preferences>) {

    object PreferencesKeys {
        val KEY_IS_DARK_THEME = booleanPreferencesKey("key_is_dark_theme")
        val KEY_USER_TOKEN = stringPreferencesKey("key_user_token")
    }

    val isDarkThemeFlow: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.KEY_IS_DARK_THEME] ?: false
        }

    val userTokenFlow: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.KEY_USER_TOKEN]
        }

    suspend fun setUserToken(token: String?) {
        dataStore.edit { preferences ->
            if (token != null) {
                preferences[PreferencesKeys.KEY_USER_TOKEN] = token
            } else {
                preferences.remove(PreferencesKeys.KEY_USER_TOKEN)
            }
        }
    }

    suspend fun setDarkTheme(isDark: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_IS_DARK_THEME] = isDark
        }
    }
}
