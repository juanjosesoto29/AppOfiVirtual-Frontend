package com.example.ofivirtualapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Esta Factory es necesaria porque SettingsViewModel requiere el contexto de la
 * aplicación (Application) para poder funcionar con SharedPreferences.
 */
class SettingsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Comprueba si la clase que se pide crear es SettingsViewModel
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            // Si lo es, crea una instancia de SettingsViewModel pasándole la 'application'
            return SettingsViewModel(application) as T
        }
        // Si se intenta usar esta Factory para crear otro ViewModel, lanza un error.
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
