package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    items: List<ServicioUI>,
    subtotalCLP: Int,
    onRemoveItem: (ServicioUI) -> Unit,
    onPay: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito", fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = {}) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } }
            )
        },
        bottomBar = {

            Surface(shadowElevation = 8.dp) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal", style = MaterialTheme.typography.titleMedium)
                        Text(subtotalCLP.toCLP(), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                    Button(
                        onClick = { onPay(subtotalCLP) },
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        enabled = items.isNotEmpty(),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text("Continuar al Pago", fontSize = 16.sp)
                    }
                }
            }
        }
    ) { innerPadding ->
        if (items.isEmpty()) {
            EmptyState()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(items, key = { it.id }) { item ->
                    CartItemCard(
                        item = item,
                        onRemoveClick = { onRemoveItem(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(
    item: ServicioUI,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        when (item.nombre) {
            "PLAN_PERSONALIZADO" -> PlanPersonalizadoCardContent(item, onRemoveClick)
            else -> ServicioSimpleCardContent(item, onRemoveClick)
        }
    }
}

@Composable
private fun ServicioSimpleCardContent(item: ServicioUI, onRemoveClick: () -> Unit) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(item.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(
                item.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Text(item.precioCLP.toCLP(), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
        Spacer(Modifier.width(8.dp))
        IconButton(onClick = onRemoveClick) {
            Icon(
                imageVector = Icons.Default.DeleteOutline,
                contentDescription = "Eliminar ${item.nombre}",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}


@Composable
private fun PlanPersonalizadoCardContent(item: ServicioUI, onRemoveClick: () -> Unit) {
    val detalles = item.descripcion.split("|")

    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text("Plan Personalizado", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Detalle de tu plan:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }

            IconButton(onClick = onRemoveClick, modifier = Modifier.offset(x = 8.dp, y = (-8).dp)) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Eliminar Plan Personalizado",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))


        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            detalles.forEach { detalleString ->
                // Separamos "Nombre del servicio: Precio"
                val parts = detalleString.split(":")
                val nombreServicio = parts.getOrNull(0)?.trim() ?: ""
                val precioServicio = parts.getOrNull(1)?.trim() ?: ""

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(nombreServicio, style = MaterialTheme.typography.bodyMedium)
                    Text(precioServicio, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))

        // --- Total del Plan ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Total del Plan", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(item.precioCLP.toCLP(), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Tu carrito está vacío",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

private fun Int.toCLP(): String {
    return "%,d".format(this).replace(",", ".") + " CLP"
}

