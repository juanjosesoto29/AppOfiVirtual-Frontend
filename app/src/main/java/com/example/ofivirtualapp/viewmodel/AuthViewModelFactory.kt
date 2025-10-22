package com.example.ofivirtualapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ofivirtualapp.repository.AuthRepository

/**
 * Esta clase es una "fábrica" que sabe cómo crear nuestro AuthViewModel.
 * Es necesaria porque AuthViewModel tiene dependencias (el AuthRepository) en su constructor.
 */
class AuthViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Comprueba si la clase que se pide es AuthViewModel
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            // Si lo es, devuelve una nueva instancia con el repositorio
            return AuthViewModel(repository) as T
        }
        // Si se pide crear otro tipo de ViewModel, lanza un error.
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
