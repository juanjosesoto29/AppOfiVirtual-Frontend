package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// ================== MODELO DE DATOS ==================
private data class Notificacion(
    val id: Int,
    val titulo: String,
    val mensaje: String,
    val fecha: String,
    val tipo: TipoNotificacion,
    val leida: Boolean = false
)

private enum class TipoNotificacion(val icon: ImageVector, val color: Color) {
    CONTRATO(Icons.Outlined.Description, Color(0xFF4CAF50)),       // Verde
    PAGO(Icons.Outlined.CreditCard, Color(0xFF2196F3)),          // Azul
    SOPORTE(Icons.Outlined.SupportAgent, Color(0xFFFF9800)),     // Naranja
    GENERAL(Icons.Outlined.Campaign, Color(0xFF673AB7))          // Morado
}

// ================== PUBLIC API ==================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacionesScreen(
    onNavigateBack: () -> Unit
) {
    // Lista de notificaciones de ejemplo
    var notificaciones by remember {
        mutableStateOf(
            listOf(
                Notificacion(1, "Contrato Vigente", "Tu Plan Anual de Oficina Virtual ha sido renovado.", "Hace 2h", TipoNotificacion.CONTRATO, leida = false),
                Notificacion(2, "Pago Recibido", "Recibimos tu pago de CLP 126.000. ¡Gracias!", "Ayer", TipoNotificacion.PAGO, leida = false),
                Notificacion(3, "Soporte Actualizado", "Tu ticket #5823 ha sido respondido.", "Ayer", TipoNotificacion.SOPORTE, leida = true),
                Notificacion(4, "Nuevos Servicios", "Hemos añadido nuevos servicios de contabilidad. ¡Échales un vistazo!", "01/10/2025", TipoNotificacion.GENERAL, leida = true),
                Notificacion(5, "Recordatorio de Pago", "Tu servicio de Contabilidad Mensual vence en 3 días.", "28/09/2025", TipoNotificacion.PAGO, leida = true)
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificaciones", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // Botón para marcar todas como leídas
                    IconButton(onClick = {
                        notificaciones = notificaciones.map { it.copy(leida = true) }
                    }) {
                        Icon(Icons.Outlined.DoneAll, contentDescription = "Marcar todo como leído")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (notificaciones.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No tienes notificaciones", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(notificaciones, key = { it.id }) { notificacion ->
                    NotificacionItem(
                        notificacion = notificacion,
                        onClick = {
                            // Marcar como leída/no leída al hacer clic
                            notificaciones = notificaciones.map {
                                if (it.id == notificacion.id) it.copy(leida = !it.leida) else it
                            }
                        }
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}


// ================== UI COMPONENT ==================
@Composable
private fun NotificacionItem(
    notificacion: Notificacion,
    onClick: () -> Unit
) {
    val backgroundColor = if (notificacion.leida) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(notificacion.tipo.color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = notificacion.tipo.icon,
                contentDescription = null,
                tint = notificacion.tipo.color,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                notificacion.titulo,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (notificacion.leida) FontWeight.Normal else FontWeight.SemiBold
            )
            Text(
                notificacion.mensaje,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.width(8.dp))

        Text(
            notificacion.fecha,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

