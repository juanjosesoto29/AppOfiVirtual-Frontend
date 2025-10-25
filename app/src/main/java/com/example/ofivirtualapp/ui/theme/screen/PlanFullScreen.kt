package com.example.ofivirtualapp.ui.theme.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

    // --- Definición de los servicios y opciones ---
    val serviciosFormalizacionDisponibles = remember {
        listOf(
            PlanOption("Creación de Empresa", 60_000),
            PlanOption("Inicio Actividades", 30_000),
            PlanOption("Certificado Digital", 25_000),
            PlanOption("Gestión de Patente", 75_000),
            PlanOption("Verificación Actividades", precio = 40_000, precioOriginal = 50_000)
        )
    }
    val opcionesOficinaVirtual = remember {
        listOf(
            PlanOption("Sin Oficina Virtual", 0),
            PlanOption("Plan Semestral", 86_000),
            PlanOption("Plan Anual", 126_000)
        )
    }

    // --- ESTADO DE LA SELECCIÓN ---
    var oficinaSeleccionada by remember { mutableStateOf(opcionesOficinaVirtual.first()) }
    var serviciosFormalizacionSeleccionados by remember { mutableStateOf(setOf<PlanOption>()) }

    // --- LÓGICA DE NEGOCIO ---
    val patenteSePuedeSeleccionar = oficinaSeleccionada.precio > 0
    LaunchedEffect(patenteSePuedeSeleccionar) {
        if (!patenteSePuedeSeleccionar) {
            // Si el usuario deselecciona la OV, quitamos la patente automáticamente.
            serviciosFormalizacionSeleccionados = serviciosFormalizacionSeleccionados.filter { it.nombre != "Gestión de Patente" }.toSet()
        }
    }

    // --- CÁLCULOS DE PRECIOS DINÁMICOS ---
    val subtotalFormalizacion = serviciosFormalizacionSeleccionados.sumOf { it.precio }
    val totalSinDescuento = subtotalFormalizacion + oficinaSeleccionada.precio
    val descuentoPorcentaje = if (oficinaSeleccionada.precio > 0 && serviciosFormalizacionSeleccionados.isNotEmpty()) 0.15f else 0f
    val montoDescuento = (totalSinDescuento * descuentoPorcentaje).toInt()
    val precioFinal = totalSinDescuento - montoDescuento
    val puedeAgregarAlCarrito = precioFinal > 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurar Plan", fontWeight = FontWeight.SemiBold) },
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
                            if (montoDescuento > 0) {
                                Text(
                                    totalSinDescuento.toCLP(),
                                    style = MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.LineThrough),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
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
                            // 1. Creamos una lista de strings con todos los detalles del plan.
                            val detalles = mutableListOf<String>()

                            // 2. Agregamos la Oficina Virtual si fue seleccionada.
                            if (oficinaSeleccionada.precio > 0) {
                                detalles.add("${oficinaSeleccionada.nombre}: ${oficinaSeleccionada.precio.toCLP()}")
                            }

                            // 3. Agregamos cada servicio de formalización seleccionado.
                            serviciosFormalizacionSeleccionados.forEach { servicio ->
                                detalles.add("${servicio.nombre}: ${servicio.precio.toCLP()}")
                            }

                            // 4. Unimos todo en un solo string, separado por un carácter especial.
                            val descripcionDetallada = detalles.joinToString("|")

                            // 5. Creamos el ServicioUI con un nombre especial y la nueva descripción.
                            val servicioFinal = ServicioUI(
                                categoria = CategoriaServicio.FORMALIZACION,
                                nombre = "PLAN_PERSONALIZADO", // Identificador único para el carrito
                                descripcion = descripcionDetallada, // Descripción detallada
                                precioCLP = precioFinal
                            )

                            onAddToCart(servicioFinal)
                            Toast.makeText(context, "Plan agregado al carrito", Toast.LENGTH_SHORT).show()
                            onNavigateBack()
                        },
                        enabled = puedeAgregarAlCarrito,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
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

            // --- Sección de servicios de Formalización con Checkboxes ---
            Spacer(Modifier.height(16.dp))
            Text("2. Elige tus servicios de Formalización", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold))
            Spacer(Modifier.height(12.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(Modifier
                    .padding(vertical = 8.dp)
                    .animateContentSize()) {
                    serviciosFormalizacionDisponibles.forEach { servicio ->
                        val isChecked = serviciosFormalizacionSeleccionados.contains(servicio)
                        val isEnabled = if (servicio.nombre == "Gestión de Patente") patenteSePuedeSeleccionar else true

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = isEnabled) {
                                    serviciosFormalizacionSeleccionados = if (isChecked) {
                                        serviciosFormalizacionSeleccionados - servicio
                                    } else {
                                        serviciosFormalizacionSeleccionados + servicio
                                    }
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = null, // La lógica está en el `clickable` del Row
                                enabled = isEnabled
                            )
                            Spacer(Modifier.width(16.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    text = servicio.nombre,
                                    color = if (isEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                                )
                                if (servicio.nombre == "Gestión de Patente" && !patenteSePuedeSeleccionar) {
                                    Text(
                                        text = "Requiere plan de Oficina Virtual",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                    )
                                }
                            }
                            // Precios
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (servicio.precioOriginal != null) {
                                    Text(
                                        servicio.precioOriginal.toCLP(),
                                        style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough),
                                        color = if (isEnabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                                    )
                                }
                                Text(
                                    servicio.precio.toCLP(),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = if (isEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                                )
                            }
                        }
                    }
                    Divider(Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal Formalización", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                        AnimatedContent(targetState = subtotalFormalizacion, label = "subtotalAnim") { subtotal ->
                            Text(
                                subtotal.toCLP(),
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }


            // PEGA ESTA NUEVA TARJETA DE RESUMEN MEJORADA

            /* --- 3. Resumen y Descuento Aplicado --- */
            Spacer(Modifier.height(24.dp))
            Text("3. Resumen de tu Plan", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold))
            Spacer(Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .animateContentSize() // Anima los cambios de tamaño
                ) {
                    // 🔹 1. LISTADO DINÁMICO DE SERVICIOS SELECCIONADOS 🔹
                    // Itera sobre la Oficina Virtual si fue seleccionada
                    if (oficinaSeleccionada.precio > 0) {
                        SummaryItemRow(label = oficinaSeleccionada.nombre, value = oficinaSeleccionada.precio.toCLP())
                    }
                    // Itera sobre los servicios de formalización seleccionados
                    serviciosFormalizacionSeleccionados.forEach { servicio ->
                        SummaryItemRow(label = servicio.nombre, value = servicio.precio.toCLP())
                    }

                    // --- Separador y totales (solo si hay items) ---
                    if (puedeAgregarAlCarrito) { // puedeAgregarAlCarrito es 'precioFinal > 0'
                        Spacer(Modifier.height(12.dp))
                        Divider()
                        Spacer(Modifier.height(12.dp))

                        // Muestra el total sin descuento SOLO si hay un descuento aplicado
                        if (montoDescuento > 0) {
                            InfoRow("Total sin descuento:", totalSinDescuento.toCLP(), isSubtle = true)
                            InfoRow("Descuento Especial (15%):", "-${montoDescuento.toCLP()}", color = Color(0xFF1E88E5))
                        }
                    } else {
                        // Mensaje cuando no hay nada seleccionado
                        Text(
                            "Selecciona un servicio para ver el resumen.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }

        }
    }
}


// --- COMPONENTES REUTILIZABLES ---

@Composable
private fun OpcionPlanCard(opcion: PlanOption, seleccionado: Boolean, onSelect: () -> Unit) {
    val scale by animateFloatAsState(targetValue = 1.0f, label = "scaleAnim")
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
            Spacer(Modifier.width(16.dp))
            Text(opcion.nombre, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
            if (opcion.precio > 0) {
                Text(opcion.precio.toCLP(), style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            }
        }
    }
}

// PEGA ESTE NUEVO COMPONENTE AL FINAL DEL ARCHIVO

/**
 * Un Composable para mostrar cada ítem en la lista de resumen.
 * Usa un punto (•) para darle aspecto de lista.
 */
@Composable
private fun SummaryItemRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "• $label", // Añade un bullet point
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.End),
            modifier = Modifier.padding(start = 8.dp) // Espacio entre etiqueta y precio
        )
    }
}

// 🔹 OPCIONAL PERO RECOMENDADO: MODIFICAR InfoRow para darle más estilo 🔹
// Reemplaza tu InfoRow actual con este para poder tener texto sutil.
@Composable
private fun InfoRow(label: String, value: String, color: Color = Color.Unspecified, isSubtle: Boolean = false) {
    val textStyle = if (isSubtle) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodyLarge
    val valueColor = if (color != Color.Unspecified) color else if (isSubtle) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = textStyle,
            modifier = Modifier.weight(1f),
            color = if (isSubtle) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = textStyle.copy(
                fontWeight = if (!isSubtle) FontWeight.SemiBold else FontWeight.Normal,
                textAlign = TextAlign.End
            ),
            color = valueColor,
            modifier = Modifier.weight(1f)
        )
    }
}



// Función helper para formatear CLP
private fun Int.toCLP(): String {
    return "%,d".format(this).replace(",", ".") + " CLP"
}

