package com.example.ofivirtualapp.data.local.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// El nombre 'user_prefs' se mantiene igual, no hay riesgo de perder datos.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    // --- Claves para las preferencias ---
    private companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        // 🔹 1. NUEVA CLAVE PARA EL EMAIL DEL USUARIO 🔹
        val USER_EMAIL = stringPreferencesKey("user_email")
    }

    // --- Métodos para la sesión ---

    // Obtener el estado de la sesión (sin cambios)
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    // Guardar el estado de la sesión (sin cambios)
    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }

    // 🔹 2. NUEVO: Guardar el email del usuario 🔹
    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
        }
    }

    // 🔹 3. NUEVO: Leer el email del usuario 🔹
    val userEmail: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL]
    }

    // 🔹 4. NUEVO: Limpiar toda la sesión (más robusto) 🔹
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(IS_LOGGED_IN)
            preferences.remove(USER_EMAIL)
        }
    }
}
