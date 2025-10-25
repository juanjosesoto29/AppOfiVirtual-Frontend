package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ofivirtualapp.navigation.Route
import com.example.ofivirtualapp.ui.theme.screen.ServicioUI
import com.example.ofivirtualapp.ui.theme.screen.CategoriaServicio


@Composable
fun ServiciosScreen(
    onAddToCart: (ServicioUI) -> Unit = {},
    onGoToPlanFull: () -> Unit = {}
) {
    val OfiBlue = Color(0xFF071290)

    // --- ESTADO PARA EL DIÁLOGO DE CONTABILIDAD LABORAL ---
    var showDialogContabilidad by remember { mutableStateOf(false) }
    var servicioParaDialogo by remember { mutableStateOf<ServicioUI?>(null) }

    val servicios = remember {
        listOf(
            // ... Oficina Virtual y Formalización sin cambios ...
            ServicioUI(
                categoria = CategoriaServicio.OFICINA_VIRTUAL,
                nombre = "Plan Semestral",
                descripcion = "Incluye domicilio tributario y recepción de correspondencia.",
                precioCLP = 86_000
            ),
            ServicioUI(
                categoria = CategoriaServicio.OFICINA_VIRTUAL,
                nombre = "Plan Anual",
                descripcion = "Incluye domicilio tributario y recepción de correspondencia. Precio preferente.",
                precioCLP = 126_000
            ),
            ServicioUI(
                categoria = CategoriaServicio.FORMALIZACION,
                nombre = "Creación de Empresa",
                descripcion = "Redacción profesional de estatutos y constitución.",
                precioCLP = 60_000
            ),
            ServicioUI(
                categoria = CategoriaServicio.FORMALIZACION,
                nombre = "Inicio Actividades",
                descripcion = "Obtención de clave secreta del SII.",
                precioCLP = 30_000
            ),
            ServicioUI(
                categoria = CategoriaServicio.FORMALIZACION,
                nombre = "Certificado Digital",
                descripcion = "Firma electrónica simple para facturación y trámites.",
                precioCLP = 20_000
            ),
            ServicioUI(
                categoria = CategoriaServicio.FORMALIZACION,
                nombre = "Gestión de Patente",
                descripcion = "Tramitación de patente comercial en la municipalidad correspondiente.",
                precioCLP = 50_000
            ),
            ServicioUI(
                categoria = CategoriaServicio.FORMALIZACION,
                nombre = "Verificación Actividades",
                descripcion = "Acreditación del domicilio tributario ante el SII.",
                precioCLP = 50_000
            ),
            ServicioUI(
                categoria = CategoriaServicio.FORMALIZACION,
                nombre = "Plan Full",
                descripcion = "Paquete configurable que incluye todos los servicios de formalización más un plan de oficina virtual a elección, con un descuento especial.",
                precioCLP = -1 // Precio dinámico
            ),
            // ... Contabilidad ...
            ServicioUI(
                categoria = CategoriaServicio.CONTABILIDAD,
                nombre = "Contabilidad Mensual",
                descripcion = "Registro, declaraciones y cumplimiento mensual.",
                precioCLP = 40_000
            ),
            // --- CAMBIO EN CONTABILIDAD LABORAL ---
            ServicioUI(
                categoria = CategoriaServicio.CONTABILIDAD,
                nombre = "Contabilidad Laboral",
                descripcion = "Gestión de contratos, liquidaciones y cotizaciones por trabajador.",
                precioCLP = 12_000, // <- Este es el precio POR PERSONA
                esPorPersona = true // <- NUEVO CAMPO para identificarlo
            ),
            ServicioUI(
                categoria = CategoriaServicio.CONTABILIDAD,
                nombre = "Renta Empresarial",
                descripcion = "Preparación y declaración anual de la renta para tu empresa.",
                precioCLP = 100_000
            ),
            ServicioUI(
                categoria = CategoriaServicio.CONTABILIDAD,
                nombre = "Renta Personal",
                descripcion = "Declaración de renta para socios y personas naturales.",
                precioCLP = 30_000
            ),
            ServicioUI(
                categoria = CategoriaServicio.CONTABILIDAD,
                nombre = "Libro Accionista SpA",
                descripcion = "Mantenimiento y actualización del registro de accionistas.",
                precioCLP = 20_000
            )
        )
    }

    // --- DIÁLOGO ---
    if (showDialogContabilidad && servicioParaDialogo != null) {
        ContabilidadLaboralDialog(
            servicioBase = servicioParaDialogo!!,
            onDismiss = { showDialogContabilidad = false },
            onConfirm = { servicioFinal ->
                onAddToCart(servicioFinal)
                showDialogContabilidad = false
            }
        )
    }

    val ordenCategorias = listOf(
        CategoriaServicio.OFICINA_VIRTUAL,
        CategoriaServicio.FORMALIZACION,
        CategoriaServicio.CONTABILIDAD
    )

    val agrupado = remember(servicios) {
        ordenCategorias.associateWith { cat ->
            servicios.filter { it.categoria == cat }.sortedBy { it.precioCLP }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Nuestros Servicios",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(16.dp))

        ordenCategorias.forEachIndexed { i, categoria ->
            val lista = agrupado[categoria].orEmpty()
            if (lista.isNotEmpty()) {
                // REEMPLAZA EL TEXT ANTERIOR CON ESTE BLOQUE 'WHEN'
                Text(
                    text = when (categoria) {
                        CategoriaServicio.OFICINA_VIRTUAL -> "Oficina Virtual"
                        CategoriaServicio.FORMALIZACION -> "Formalización"
                        CategoriaServicio.CONTABILIDAD -> "Contabilidad"
                    },
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(Modifier.height(10.dp))
                lista.forEachIndexed { idx, s ->
                    // --- LÓGICA CONDICIONAL PARA SERVICIOS ESPECIALES ---
                    when {
                        // Caso 1: Plan Full
                        s.nombre == "Plan Full" -> {
                            ServicioCard(
                                servicio = s,
                                colorBoton = OfiBlue,
                                onAdd = onGoToPlanFull,
                                textoBoton = "Configurar Plan"
                            )
                        }
                        // Caso 2: Contabilidad Laboral por persona
                        s.esPorPersona -> {
                            ServicioCard(
                                servicio = s,
                                colorBoton = OfiBlue,
                                onAdd = {
                                    servicioParaDialogo = s
                                    showDialogContabilidad = true
                                },
                                textoBoton = "Seleccionar"
                            )
                        }
                        // Caso 3: Servicio normal
                        else -> {
                            ServicioCard(
                                servicio = s,
                                colorBoton = OfiBlue,
                                onAdd = { onAddToCart(s) }
                            )
                        }
                    }
                    if (idx != lista.lastIndex) Spacer(Modifier.height(12.dp))
                }
                if (i != ordenCategorias.lastIndex) Spacer(Modifier.height(18.dp))
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}


/* ----------------- UI ----------------- */

@Composable
private fun ServicioCard(
    servicio: ServicioUI,
    colorBoton: Color,
    onAdd: () -> Unit,
    textoBoton: String = "Agregar al carrito"
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Text(
                    servicio.nombre,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )

                if (servicio.precioCLP >= 0) {
                    val priceText = if (servicio.esPorPersona) "${servicio.precioCLP.toCLP()} c/u" else servicio.precioCLP.toCLP()
                    Text(
                        text = priceText,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                } else {
                    Text(
                        text = "Precio variable",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    )
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                servicio.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(14.dp))
            Button(
                onClick = onAdd,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorBoton, contentColor = Color.White),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(textoBoton, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

/* ----------------- DIÁLOGO ESPECÍFICO ----------------- */
@Composable
private fun ContabilidadLaboralDialog(
    servicioBase: ServicioUI,
    onDismiss: () -> Unit,
    onConfirm: (ServicioUI) -> Unit
) {
    var cantidadPersonas by remember { mutableStateOf(1) }
    val precioTotal = servicioBase.precioCLP * cantidadPersonas

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(servicioBase.nombre, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("Selecciona el número de trabajadores para calcular el precio final.")
                Spacer(Modifier.height(16.dp))
                // --- Selector de cantidad ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { if (cantidadPersonas > 1) cantidadPersonas-- }) {
                        Icon(Icons.Default.Remove, "Quitar")
                    }
                    Text(
                        text = "$cantidadPersonas",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    IconButton(onClick = { cantidadPersonas++ }) {
                        Icon(Icons.Default.Add, "Añadir")
                    }
                }
                Spacer(Modifier.height(16.dp))
                // --- Precio final ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Precio Total:", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleLarge)
                    Text(precioTotal.toCLP(), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                // Creamos una nueva instancia del servicio con el precio y nombre actualizados
                val servicioFinal = servicioBase.copy(
                    nombre = "${servicioBase.nombre} (x$cantidadPersonas)",
                    precioCLP = precioTotal,
                    esPorPersona = false // Lo marcamos como falso para que no vuelva a abrir el diálogo en el carrito
                )
                onConfirm(servicioFinal)
            }) {
                Text("Confirmar y Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}


/* ----------------- Utils ----------------- */

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

