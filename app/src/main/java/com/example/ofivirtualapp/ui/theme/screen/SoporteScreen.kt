package com.example.ofivirtualapp.ui.theme.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ofivirtualapp.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoporteScreen(
    onNavigateBack: () -> Unit,
    onGoToFaq: () -> Unit
) {
    val context = LocalContext.current

    val whatsappNumber = "+56928532988"
    val supportEmail = "soporte@ofivirtual.cl"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ayuda y Soporte", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "¿Necesitas ayuda? Elige una de las siguientes opciones para resolver tus dudas o contactarnos directamente.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            SupportSection(title = "Auto-ayuda") {
                SupportItem(
                    icon = Icons.Outlined.Quiz,
                    title = "Preguntas Frecuentes (FAQ)",
                    subtitle = "Encuentra respuestas a las dudas más comunes.",
                    onClick = onGoToFaq
                )
            }

            SupportSection(title = "Contacto Directo") {
                SupportItem(
                    icon = Icons.Outlined.Call,
                    title = "WhatsApp",
                    subtitle = "Inicia un chat con un agente de soporte.",
                    onClick = { openUrl(context, "https://wa.me/$whatsappNumber") }
                )
                Divider(Modifier.padding(horizontal = 16.dp))
                SupportItem(
                    icon = Icons.Outlined.AlternateEmail,
                    title = "Correo Electrónico",
                    subtitle = "Envíanos tu consulta por email.",
                    onClick = { sendEmail(context, supportEmail, "Consulta desde la App OfiVirtual") }
                )
            }
        }
    }
}


@Composable
private fun SupportSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.fillMaxWidth()) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 4.dp)
            )
            content()
        }
    }
}

@Composable
private fun SupportItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}


private fun openUrl(context: android.content.Context, url: String) {
    runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}

private fun sendEmail(context: android.content.Context, to: String, subject: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        putExtra(Intent.EXTRA_SUBJECT, subject)
    }
    runCatching {
        context.startActivity(intent)
    }
}
