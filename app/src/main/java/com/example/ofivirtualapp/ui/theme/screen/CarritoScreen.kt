package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- 1. MODELO DE DATOS (AHORA CON ID ÚNICO) ---
data class CartItem(
    val id: String, // ID para poder eliminarlo
    val title: String,
    val subtitle: String,
    val priceCLP: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    items: List<CartItem> = emptyList(),
    subtotalCLP: Int = 0,
    onRemoveItem: (String) -> Unit = {}, // Acción para eliminar
    onPay: (Int) -> Unit = {}
) {
    val OfiBlue = Color(0xFF071290)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Carrito", fontWeight = FontWeight.SemiBold) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // -- Contenido que se desplaza (LazyColumn es más eficiente para listas) --
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (items.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxSize() // Ocupa todo el espacio de LazyColumn
                                .padding(bottom = 100.dp), // Ajuste para que no quede tan bajo
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Tu carrito está vacío",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(items, key = { it.id }) { item ->
                        CartItemCard(
                            item = item,
                            onRemove = { onRemoveItem(item.id) }
                        )
                    }
                }
            }

            // --- CONTENIDO FIJO INFERIOR ---
            if (items.isNotEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp // Sombra para separar del contenido
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Subtotal", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                            Text(subtotalCLP.toCLP(), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                        }
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { onPay(subtotalCLP) },
                            modifier = Modifier.fillMaxWidth().height(54.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = OfiBlue, contentColor = Color.White)
                        ) {
                            Text("Ir a pagar", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

// --- SUBCOMPOSABLE CON BOTÓN DE ELIMINAR ---
@Composable
private fun CartItemCard(
    item: CartItem,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(item.title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(item.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            Spacer(Modifier.width(12.dp))
            Text(item.priceCLP.toCLP(), style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar del carrito", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}


// --- FUNCIÓN DE UTILIDAD (SIN CAMBIOS) ---
private fun Int.toCLP(): String {
    if (this == 0) return "$0"
    val s = this.toString()
    val sb = StringBuilder()
    var count = 0
    for (i in s.length - 1 downTo 0) {
        sb.append(s[i])
        count++
        if (count == 3 && i != 0) {
            sb.append('.')
            count = 0
        }
    }
    return "$" + sb.reverse().toString()
}
