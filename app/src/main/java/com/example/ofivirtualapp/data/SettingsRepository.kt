package com.example.ofivirtualapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    private object Keys {
        val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val REMEMBER_ME = booleanPreferencesKey("remember_me")
        val THEME_MODE = stringPreferencesKey("theme_mode") // "light" | "dark" | "system"
    }

    // Lecturas (Flow)
    val onboardingDone: Flow<Boolean> =
        dataStore.data.map { it[Keys.ONBOARDING_DONE] ?: false }

    val authToken: Flow<String?> =
        dataStore.data.map { it[Keys.AUTH_TOKEN] }

    val rememberMe: Flow<Boolean> =
        dataStore.data.map { it[Keys.REMEMBER_ME] ?: false }

    val themeMode: Flow<String> =
        dataStore.data.map { it[Keys.THEME_MODE] ?: "system" }

    // Escrituras (suspend)
    suspend fun setOnboardingDone(done: Boolean) {
        dataStore.edit { it[Keys.ONBOARDING_DONE] = done }
    }

    suspend fun setAuthToken(token: String?) {
        dataStore.edit { prefs ->
            if (token.isNullOrBlank()) prefs.remove(Keys.AUTH_TOKEN)
            else prefs[Keys.AUTH_TOKEN] = token
        }
    }

    suspend fun setRememberMe(remember: Boolean) {
        dataStore.edit { it[Keys.REMEMBER_ME] = remember }
    }

    suspend fun setThemeMode(mode: String) {
        dataStore.edit { it[Keys.THEME_MODE] = mode }
    }
}
