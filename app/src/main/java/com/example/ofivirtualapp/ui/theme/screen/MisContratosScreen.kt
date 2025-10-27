package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- 1. Modelo de datos para esta pantalla ---
data class ContratoInfo(
    val tipo: String,
    val nombre: String,
    val fechaEmision: String,
    val estaVigente: Boolean,
    val url: String // Link para abrir el contrato
)

// --- 2. El Composable principal de la pantalla ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisContratosScreen(
    onNavigateBack: () -> Unit,
    onOpenContrato: (String) -> Unit // Función para abrir el link
) {
    // Lista de ejemplo de los contratos del usuario.
    // En un futuro, estos datos vendrían del ViewModel.
    val listaDeContratos = listOf(
        ContratoInfo(
            tipo = "O",
            nombre = "Contrato Oficina Virtual",
            fechaEmision = "Emitido 13/12/2026",
            estaVigente = true,
            url = "https://drive.google.com/file/d/1U2JMb9S1laCQOtB8c9JCX0pNOb5TrzFl/view?usp=sharing"
        ),
        ContratoInfo(
            tipo = "C",
            nombre = "Contrato Contabilidad",
            fechaEmision = "Emitido 05/03/2025",
            estaVigente = false,
            url = "https://ofivirtual.cl/contrato-ejemplo-1.pdf"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Contratos", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        // Usamos LazyColumn para un rendimiento óptimo si la lista crece.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listaDeContratos) { contrato ->
                ContratoCard(
                    contrato = contrato,
                    onClick = { onOpenContrato(contrato.url) }
                )
            }
        }
    }
}

// --- 3. El Composable para cada tarjeta de contrato ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContratoCard(
    contrato: ContratoInfo,
    onClick: () -> Unit
) {
    val badgeGreen = Color(0xFF34C759)
    val badgeGrey = Color(0xFF9AA3AF)

    Card(
        onClick = onClick, // La tarjeta es clicable
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono con la inicial (O, C, F...)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    contrato.tipo,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(Modifier.width(16.dp))

            // Nombre y fecha
            Column(Modifier.weight(1f)) {
                Text(
                    contrato.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    contrato.fechaEmision,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.width(8.dp))

            // Chip de estado (VIGENTE / VENCIDO)
            StatusChip(
                label = if (contrato.estaVigente) "VIGENTE" else "VENCIDO",
                bg = if (contrato.estaVigente) badgeGreen else badgeGrey,
                fg = Color.White
            )
        }
    }
}

// Re-utilizamos el Composable StatusChip que ya tienes en otras pantallas
@Composable
private fun StatusChip(label: String, bg: Color, fg: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = fg, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
