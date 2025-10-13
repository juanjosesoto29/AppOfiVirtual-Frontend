package com.example.ofivirtualapp.ui.theme.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ofivirtualapp.navigation.Route

// --- Modelos de datos para esta pantalla ---
private data class PlanOption(val nombre: String, val precio: Int, val precioOriginal: Int? = null)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanFullScreen(
    onNavigateBack: () -> Unit,
    onAddToCart: (ServicioUI) -> Unit
) {
    val context = LocalContext.current
    val OfiBlue = Color(0xFF071290)

    // --- Definición de los servicios incluidos y las opciones ---
    val serviciosFormalizacion = remember {
        listOf(
            PlanOption("Creación de Empresa", 60_000),
            PlanOption("Inicio Actividades", 30_000),
            PlanOption("Certificado Digital", 25_000),
            PlanOption("Gestión de Patente", 75_000),
            // <--- CAMBIO AQUÍ: Precio especial para Verificación de Actividades ---
            PlanOption(
                "Verificación Actividades",
                precio = 40_000,       // Precio con descuento
                precioOriginal = 50_000 // Guardamos el precio original para mostrarlo
            )
        )
    }
    val opcionesOficinaVirtual = remember {
        listOf(
            PlanOption("Plan Semestral", 86_000),
            PlanOption("Plan Anual", 126_000)
        )
    }
    val descuentoPorcentaje = 0.15f // 15% de descuento

    // --- Estado de la selección ---
    var oficinaSeleccionada by remember { mutableStateOf(opcionesOficinaVirtual.first()) }

    // --- Cálculos de precios ---
    val subtotalFormalizacion = remember(serviciosFormalizacion) { serviciosFormalizacion.sumOf { it.precio } }
    val totalSinDescuento = subtotalFormalizacion + oficinaSeleccionada.precio
    val montoDescuento = (totalSinDescuento * descuentoPorcentaje).toInt()
    val precioFinal = totalSinDescuento - montoDescuento

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurar Plan Full", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Precio Final", style = MaterialTheme.typography.titleMedium)
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                totalSinDescuento.toCLP(),
                                style = MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.LineThrough),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            AnimatedContent(targetState = precioFinal, label = "precioFinalAnim") { precio ->
                                Text(
                                    precio.toCLP(),
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = OfiBlue)
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val servicioFinal = ServicioUI(
                                categoria = CategoriaServicio.FORMALIZACION,
                                nombre = "Plan Full (con OV ${oficinaSeleccionada.nombre})",
                                descripcion = "Paquete con ${serviciosFormalizacion.size} servicios de formalización y 1 de oficina virtual.",
                                precioCLP = precioFinal
                            )
                            onAddToCart(servicioFinal)
                            Toast.makeText(context, "Plan Full agregado al carrito", Toast.LENGTH_SHORT).show()
                            onNavigateBack()
                        },
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OfiBlue, contentColor = Color.White)
                    ) {
                        Text("Agregar al Carrito", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // --- Sección de selección de Oficina Virtual ---
            Text("1. Elige tu plan de Oficina Virtual", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold))
            Spacer(Modifier.height(8.dp))
            Text(
                "El Plan Full combina todos los servicios de formalización con un plan de oficina virtual a un precio con descuento.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(12.dp))
            Column(Modifier.selectableGroup()) {
                opcionesOficinaVirtual.forEach { opcion ->
                    OpcionPlanCard(
                        opcion = opcion,
                        seleccionado = opcion == oficinaSeleccionada,
                        onSelect = { oficinaSeleccionada = opcion }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }

            // --- Sección de servicios incluidos ---
            Spacer(Modifier.height(16.dp))
            Text("2. Servicios de Formalización Incluidos", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold))
            Spacer(Modifier.height(12.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(Modifier.padding(16.dp)) {
                    serviciosFormalizacion.forEach { servicio ->
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(servicio.nombre, style = MaterialTheme.typography.bodyMedium)

                            // <--- CAMBIO AQUÍ: Muestra el precio tachado si hay descuento ---
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (servicio.precioOriginal != null) {
                                    Text(
                                        servicio.precioOriginal.toCLP(),
                                        style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    servicio.precio.toCLP(),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }
                        Divider(Modifier.padding(vertical = 8.dp))
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal Formalización", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                        Text(
                            subtotalFormalizacion.toCLP(),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            // --- Resumen y Descuento ---
            Spacer(Modifier.height(16.dp))
            Text("3. Resumen y Descuento Aplicado", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold))
            Spacer(Modifier.height(12.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoRow("Subtotal Formalización:", subtotalFormalizacion.toCLP())
                    AnimatedContent(targetState = oficinaSeleccionada, label = "oficinaAnim") { target ->
                        InfoRow("Oficina Virtual (${target.nombre}):", target.precio.toCLP())
                    }
                    Divider()
                    AnimatedContent(targetState = totalSinDescuento, label = "totalSinDescAnim") { target ->
                        InfoRow("Total sin descuento:", target.toCLP())
                    }
                    InfoRow("Descuento (15%):", "-${montoDescuento.toCLP()}", color = Color(0xFF1E88E5))
                }
            }
        }
    }
}

// ... (El resto del archivo: OpcionPlanCard, InfoRow, toCLP no necesitan cambios)

@Composable
private fun OpcionPlanCard(opcion: PlanOption, seleccionado: Boolean, onSelect: () -> Unit) {
    val scale by animateFloatAsState(targetValue = if (seleccionado) 1.0f else 0.98f, label = "scaleAnim")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .selectable(
                selected = seleccionado,
                onClick = onSelect,
                role = Role.RadioButton
            ),
        shape = RoundedCornerShape(16.dp),
        border = if (seleccionado) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = if (seleccionado) 4.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = seleccionado, onClick = null)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(opcion.nombre, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text(
                    opcion.precio.toCLP(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String, color: Color = Color.Unspecified) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = color)
    }
}

private fun Int.toCLP(): String {
    if (this == 0) return "$0"
    val s = this.toString()
    val sb = StringBuilder()
    var c = 0
    for (i in s.length - 1 downTo 0) {
        sb.append(s[i]); c++
        if (c == 3 && i != 0) { sb.append('.'); c = 0 }
    }
    return "$" + sb.reverse().toString()
}
