package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ofivirtualapp.R

// --- 1. El Composable principal de la pantalla ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetodosPagoScreen(
    onNavigateBack: () -> Unit,
    onOpenWhatsApp: (String) -> Unit // Recibe la función para abrir el link
) {
    val numeroWhatsapp = "+56912345678" // Reemplaza con tu número de WhatsApp
    val mensajeWhatsapp = "Hola, adjunto mi comprobante de pago."
    val linkWhatsapp = "https://wa.me/$numeroWhatsapp?text=${mensajeWhatsapp.replace(" ", "%20")}"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Métodos de Pago", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Paga por Transferencia",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))

            // --- Tarjeta con los datos de la transferencia ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoRow(label = "Beneficiario", value = "OfiVirtual SpA")
                    InfoRow(label = "RUT", value = "77.123.456-7")
                    InfoRow(label = "Banco", value = "Banco BCI")
                    InfoRow(label = "Tipo de Cuenta", value = "Cuenta Corriente")
                    InfoRow(label = "N° de Cuenta", value = "123456789")
                    InfoRow(label = "Correo", value = "pagos@ofivirtual.cl")
                    InfoRow(label = "Asunto", value = "Pago de servicios")
                }
            }

            Spacer(Modifier.height(24.dp))

            // --- Tarjeta de instrucción para WhatsApp ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¡Importante!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Una vez realizado el pago, envía tu comprobante a nuestro WhatsApp para activar tus servicios.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { onOpenWhatsApp(linkWhatsapp) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF25D366), // Color de WhatsApp
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_whatsapp), // Necesitarás un ícono de WhatsApp
                            contentDescription = "WhatsApp",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Enviar Comprobante")
                    }
                }
            }
        }
    }
}

// --- 2. Composable auxiliar para las filas de información ---
@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
