package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FormalizacionScreen(
    onAddToCart: (ServicioFormalizacion) -> Unit = {}
) {
    val OfiBlue = Color(0xFF071290)

    val servicios = remember {
        listOf(
            ServicioFormalizacion(
                nombre = "Creación de Empresa",
                descripcion = "Constitución, redacción de estatutos y registro en el Diario Oficial.",
                precioCLP = 60_000
            ),
            ServicioFormalizacion(
                nombre = "Inicio de Actividades",
                descripcion = "Obtención de clave secreta y activación del RUT ante el SII.",
                precioCLP = 30_000
            ),
            ServicioFormalizacion(
                nombre = "Modificación de Sociedad",
                descripcion = "Actualización de socios, giros o capital en el registro oficial.",
                precioCLP = 45_000
            ),
            ServicioFormalizacion(
                nombre = "Verificación de Actividades",
                descripcion = "Acreditación del domicilio tributario ante fiscalización municipal o SII.",
                precioCLP = 50_000
            ),
            ServicioFormalizacion(
                nombre = "Gestión de Patente Comercial",
                descripcion = "Solicitud y obtención del permiso de funcionamiento municipal.",
                precioCLP = 40_000
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {

        Text(
            text = "Formalización",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(Modifier.height(12.dp))


        servicios.forEachIndexed { idx, servicio ->
            ServicioFormalizacionCard(
                servicio = servicio,
                colorBoton = OfiBlue,
                onAdd = { onAddToCart(servicio) }
            )
            if (idx != servicios.lastIndex) Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.height(16.dp))
    }
}


data class ServicioFormalizacion(
    val nombre: String,
    val descripcion: String,
    val precioCLP: Int
)

@Composable
private fun ServicioFormalizacionCard(
    servicio: ServicioFormalizacion,
    colorBoton: Color,
    onAdd: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp)) {

            // 1. Nombre del servicio
            Text(
                servicio.nombre,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // 2. Precio (debajo del nombre)
            Text(
                text = servicio.precioCLP.toCLP(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            // 3. Descripción
            Spacer(Modifier.height(8.dp))
            Text(
                servicio.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 4. Botón
            Spacer(Modifier.height(14.dp))
            Button(
                onClick = onAdd,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorBoton,
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Agregar al carrito", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
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
