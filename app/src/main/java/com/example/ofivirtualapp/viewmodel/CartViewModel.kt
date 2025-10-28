package com.example.ofivirtualapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ofivirtualapp.ui.theme.screen.ServicioUI // 🔹 1. IMPORTAMOS EL MODELO CORRECTO 🔹

// Estado de la UI para el carrito. Ahora contiene una lista de ServicioUI.
data class CartUiState(
    val items: List<ServicioUI> = emptyList() //  2. USAMOS List<ServicioUI>
)

class CartViewModel : ViewModel() {

    // Flujo de estado para la UI del carrito
    private val _uiState = mutableStateOf(CartUiState())
    val uiState: State<CartUiState> = _uiState

    // Estado derivado para calcular el subtotal automáticamente
    val subtotal: State<Int> = derivedStateOf {
        _uiState.value.items.sumOf { it.precioCLP } //  3. SUMAMOS LOS PRECIOS DE ServicioUI
    }

    /**
     * Agrega un nuevo servicio al carrito.
     */
    fun addItem(servicio: ServicioUI) { //  4. LA FUNCIÓN AHORA RECIBE un ServicioUI
        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items + servicio
        )
    }

    /**
     * Elimina un servicio del carrito por su ID.
     */
    fun removeItem(servicio: ServicioUI) { //  5. LA FUNCIÓN ELIMINA un ServicioUI
        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items.filterNot { it.id == servicio.id }
        )
    }

    /**
     * Vacía completamente el carrito.
     */
    fun clearCart() {
        _uiState.value = _uiState.value.copy(items = emptyList())
    }
}
