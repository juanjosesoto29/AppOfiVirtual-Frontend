package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqScreen(
    onNavigateBack: () -> Unit
) {
    val OfiBlue = Color(0xFF071290)

    val faqs = remember {
        listOf(
            FaqItem(
                pregunta = "¿Qué es OfiVirtual?",
                respuesta = "OfiVirtual es una plataforma que centraliza servicios de oficina virtual, contabilidad y formalización de empresas. Te permite gestionar tu negocio 100% online, con respaldo contable y tributario."
            ),
            FaqItem(
                pregunta = "¿Qué incluye el servicio de Oficina Virtual?",
                respuesta = "Incluye domicilio tributario legal para tu empresa, recepción de correspondencia, notificaciones y soporte para fiscalización municipal o del SII."
            ),
            FaqItem(
                pregunta = "¿Cómo contrato un plan?",
                respuesta = "Desde la app puedes elegir el plan que más se adapte a tus necesidades en la sección 'Oficina Virtual' o 'Servicios', agregarlo al carrito y completar el pago en línea."
            ),
            FaqItem(
                pregunta = "¿Puedo emitir boletas o facturas desde OfiVirtual?",
                respuesta = "Sí. OfiVirtual te asesora en la configuración de tu facturación electrónica ante el SII para que puedas emitir documentos tributarios con tu RUT o empresa."
            ),
            FaqItem(
                pregunta = "¿Cuánto demora la formalización de mi empresa?",
                respuesta = "Normalmente entre 24 y 72 horas hábiles desde la recepción de los documentos y firma digital, dependiendo del tipo de sociedad y revisión del Registro de Empresas."
            ),
            FaqItem(
                pregunta = "¿Cómo puedo contactar soporte?",
                respuesta = "Puedes escribirnos directamente por WhatsApp o correo electrónico desde el menú de 'Perfil > Ayuda' o desde la sección 'Contacto' del sitio web."
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Preguntas Frecuentes", fontWeight = FontWeight.SemiBold) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    "Resuelve tus dudas sobre los servicios de OfiVirtual.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(6.dp))
            }

            items(faqs) { item ->
                FaqCard(
                    faq = item,
                    accent = OfiBlue
                )
            }
        }
    }
}

private data class FaqItem(
    val pregunta: String,
    val respuesta: String
)

@Composable
private fun FaqCard(
    faq: FaqItem,
    accent: Color
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    faq.pregunta,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = accent,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                    contentDescription = if (expanded) "Ocultar respuesta" else "Mostrar respuesta",
                    tint = accent
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(animationSpec = tween(150)) + expandVertically(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(150)) + shrinkVertically(animationSpec = tween(300))
            ) {
                Text(
                    text = faq.respuesta,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                )
            }
        }
    }
}
