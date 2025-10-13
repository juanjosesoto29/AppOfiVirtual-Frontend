package com.example.ofivirtualapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.asIntState
import androidx.compose.runtime.derivedStateOf
import com.example.ofivirtualapp.ui.theme.screen.CartItem
import com.example.ofivirtualapp.ui.theme.screen.ServicioUI
import java.util.UUID

// Estado de la UI del Carrito
data class CartUiState(
    val items: List<CartItem> = emptyList()
)

class CartViewModel : ViewModel() {

    private val _uiState = mutableStateOf(CartUiState())
    val uiState: State<CartUiState> = _uiState

    // Derivamos el subtotal directamente del estado de los ítems
    val subtotal: State<Int> = derivedStateOf {
        _uiState.value.items.sumOf { it.priceCLP }
    }

    /**
     * Agrega un servicio al carrito.
     * Le asigna un ID único para poder eliminarlo después.
     */
    fun addItem(servicio: ServicioUI) {
        val newItem = CartItem(
            id = UUID.randomUUID().toString(), // ID único para poder borrarlo
            title = servicio.nombre,
            subtitle = servicio.descripcion,
            priceCLP = servicio.precioCLP
        )
        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items + newItem
        )
    }

    /**
     * Elimina un ítem del carrito usando su ID único.
     */
    fun removeItem(itemId: String) {
        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items.filterNot { it.id == itemId }
        )
    }

    /**
     * Vacía el carrito por completo (útil después de un pago exitoso).
     */
    fun clearCart() {
        _uiState.value = CartUiState()
    }
}
