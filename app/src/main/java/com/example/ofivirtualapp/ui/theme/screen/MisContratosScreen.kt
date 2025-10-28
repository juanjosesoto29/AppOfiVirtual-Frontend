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


data class ContratoInfo(
    val tipo: String,
    val nombre: String,
    val fechaEmision: String,
    val estaVigente: Boolean,
    val url: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisContratosScreen(
    onNavigateBack: () -> Unit,
    onOpenContrato: (String) -> Unit
) {

    val listaDeContratos = listOf(
        ContratoInfo(
            tipo = "O",
            nombre = "Contrato Oficina Virtual",
            fechaEmision = "Emitido 13/12/2026",
            estaVigente = true,
            url = "https://drive.google.com/file/d/1U2JMb9S1laCQOtB8c9JCX0pNOb5TrzFl/view?usp=sharing"
        ),
        ContratoInfo(
            tipo = "O",
            nombre = "Contrato Oficina Virtual",
            fechaEmision = "Emitido 05/03/2025",
            estaVigente = false,
            url = "https://drive.google.com/file/d/1DpVk6lXWrnLHAPf8tBAEHQMkWsZqhnhJ/view?usp=sharing"
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContratoCard(
    contrato: ContratoInfo,
    onClick: () -> Unit
) {
    val badgeGreen = Color(0xFF34C759)
    val badgeGrey = Color(0xFF9AA3AF)

    Card(
        onClick = onClick,
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

            StatusChip(
                label = if (contrato.estaVigente) "VIGENTE" else "VENCIDO",
                bg = if (contrato.estaVigente) badgeGreen else badgeGrey,
                fg = Color.White
            )
        }
    }
}

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
