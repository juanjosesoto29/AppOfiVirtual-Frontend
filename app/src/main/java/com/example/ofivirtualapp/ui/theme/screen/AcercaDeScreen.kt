package com.example.ofivirtualapp.ui.theme.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ofivirtualapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcercaDeScreen(
    onNavigateBack: () -> Unit,
    appName: String = "OfiVirtual",
    description: String = "Ofivirtual es una empresa especializada en la creación de empresas, contabilidad y oficinas virtuales. Nuestra misión es apoyar a los emprendedores y pymes en su crecimiento.",
    // Información de OfiVirtual
    websiteUrl: String = "https://ofivirtual.cl",
    email: String = "contacto@ofivirtual.cl",
    whatsapp: String = "+56912345678",
    privacyUrl: String = "https://ofivirtual.cl/politica-privacidad/",
    contacUrl: String = "https://ofivirtual.cl/contacto/",
    instagramUrl: String = "https://instagram.com/ofivirtual",
    tiktokUrl: String = "https://www.tiktok.com/@ofivirtual"
) {
    val context = LocalContext.current

    val packageInfo = remember { context.packageManager.getPackageInfo(context.packageName, 0) }
    val versionName = packageInfo.versionName
    val versionCode = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
        packageInfo.longVersionCode
    } else {
        @Suppress("DEPRECATION")
        packageInfo.versionCode.toLong()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Acerca de", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Card de Encabezado
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ofivirtual_logo),
                        contentDescription = "$appName logo",
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(appName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Versión $versionName (Build $versionCode)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // --- SECCIÓN DESTACADA (Y ÚNICA) PARA EL SITIO WEB ---
            SectionCard(title = "Sitio Web Principal") {
                LinkItem(
                    icon = Icons.Outlined.Language,
                    title = "Página Oficial de OfiVirtual",
                    subtitle = websiteUrl, // Usa la URL principal
                    onClick = { openUrl(context, websiteUrl) }
                )
            }

            Spacer(Modifier.height(16.dp))

            // --- SECCIÓN "ENLACES" (AHORA SIN EL SITIO WEB REPETIDO) ---
            SectionCard(title = "Información y Soporte") {
                LinkItem(
                    icon = Icons.Outlined.PrivacyTip,
                    title = "Política de privacidad",
                    subtitle = "Cómo protegemos tus datos",
                    onClick = { openUrl(context, privacyUrl) }
                )
                LinkItem(
                    icon = Icons.Outlined.HeadsetMic, // Ícono más claro para contacto/soporte
                    title = "Contáctanos",
                    subtitle = "Resuelve tus dudas con nosotros",
                    onClick = { openUrl(context, contacUrl) }
                )
            }

            Spacer(Modifier.height(16.dp))

            // Card de Contacto directo
            SectionCard(title = "Contacto Directo") {
                LinkItem(
                    icon = Icons.Outlined.AlternateEmail,
                    title = "Correo",
                    subtitle = email,
                    onClick = { sendEmail(context, email, "Consulta desde la app $appName") }
                )
                LinkItem(
                    icon = Icons.Outlined.Call,
                    title = "WhatsApp",
                    subtitle = whatsapp,
                    onClick = { openUrl(context, "https://wa.me/${whatsapp.filter { it.isDigit() || it == '+' }}") }
                )
            }

            Spacer(Modifier.height(16.dp))

            // Card de Redes Sociales
            SectionCard(title = "Síguenos en Redes") {
                LinkItem(
                    icon = painterResource(id = R.drawable.ic_instagram),
                    title = "Instagram",
                    subtitle = "@ofivirtual",
                    onClick = { openUrl(context, instagramUrl) }
                )
                LinkItem(
                    icon = painterResource(id = R.drawable.ic_tiktok),
                    title = "Tiktok",
                    subtitle = "@ofivirtual",
                    onClick = { openUrl(context, tiktokUrl) }
                )
                LinkItem(
                    icon = Icons.Outlined.Share,
                    title = "Compartir app",
                    subtitle = "Recomendar $appName",
                    onClick = { shareText(context, "Te recomiendo la app de OfiVirtual: $websiteUrl") }
                )
            }

            // Pie de página con Copyright
            Spacer(Modifier.weight(1f))
            Text(
                "© ${java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)} OfiVirtual. Todos los derechos reservados.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp, top = 16.dp)
            )
        }
    }
}


// El resto del archivo (LinkItem, SectionCard, y las funciones helpers) no cambia.

@Composable
private fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.fillMaxWidth().padding(14.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}

// Versión para ImageVector
@Composable
private fun LinkItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

// Versión para Painter
@Composable
private fun LinkItem(
    icon: androidx.compose.ui.graphics.painter.Painter,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

// Funciones helpers
private fun openUrl(context: android.content.Context, url: String) {
    runCatching {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}

private fun sendEmail(context: android.content.Context, to: String, subject: String = "") {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        putExtra(Intent.EXTRA_SUBJECT, subject)
    }
    runCatching { context.startActivity(intent) }
}

private fun shareText(context: android.content.Context, text: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    runCatching { context.startActivity(shareIntent) }
}

