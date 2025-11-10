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
fun ContabilidadScreen(
    onAddToCart: (ServicioConta) -> Unit = {}
) {
    val OfiBlue = Color(0xFF071290)

    val servicios = remember {
        listOf(
            // Mensual
            ServicioConta(CategoriaConta.MENSUAL, "Contabilidad Mensual", "Registro, cierre y declaraciones periódicas.", 40_000),
            ServicioConta(CategoriaConta.MENSUAL, "Asesoría Tributaria Mensual", "Plan de asesoría y revisión de cumplimiento.", 50_000),

            // Rentas
            ServicioConta(CategoriaConta.RENTAS, "Renta Personal (Honorarios)", "Preparación y envío de F22 Persona Natural.", 45_000),
            ServicioConta(CategoriaConta.RENTAS, "Renta Empresarial", "Determinación RLI, capital propio, DDJJ anuales.", 120_000),

            // Laboral
            ServicioConta(CategoriaConta.LABORAL, "Libro de Remuneraciones", "Cálculo, emisión y registro de remuneraciones.", 35_000),
            ServicioConta(CategoriaConta.LABORAL, "Contrato y Finiquito", "Redacción y gestión de documentación laboral.", 25_000),

            // Trámites SII
            ServicioConta(CategoriaConta.SII, "Declaración Mensual F29", "IVA, PPM, retenciones. Preparación y envío.", 25_000),
            ServicioConta(CategoriaConta.SII, "Declaración F50", "Declaración de retenciones específicas (Renta/IVA).", 22_000),
            ServicioConta(CategoriaConta.SII, "Actualización de Giros", "Modificación/inscripción de actividades ante SII.", 28_000)
        )
    }

    val orden = listOf(
        CategoriaConta.MENSUAL,
        CategoriaConta.RENTAS,
        CategoriaConta.LABORAL,
        CategoriaConta.SII
    )

    val agrupado = remember(servicios) {
        orden.associateWith { cat -> servicios.filter { it.categoria == cat } }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Contabilidad",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(Modifier.height(12.dp))

        orden.forEachIndexed { idxCat, cat ->
            val lista = agrupado[cat].orEmpty()
            if (lista.isNotEmpty()) {
                Text(
                    text = cat.titulo,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(Modifier.height(10.dp))

                lista.forEachIndexed { i, s ->
                    ServicioContaCard(
                        servicio = s,
                        colorBoton = OfiBlue,
                        onAdd = { onAddToCart(s) }
                    )
                    if (i != lista.lastIndex) Spacer(Modifier.height(12.dp))
                }

                if (idxCat != orden.lastIndex) Spacer(Modifier.height(18.dp))
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}


enum class CategoriaConta(val titulo: String) {
    MENSUAL("Mensual"),
    RENTAS("Rentas"),
    LABORAL("Laboral"),
    SII("Trámites SII")
}

data class ServicioConta(
    val categoria: CategoriaConta,
    val nombre: String,
    val descripcion: String,
    val precioCLP: Int
)

@Composable
private fun ServicioContaCard(
    servicio: ServicioConta,
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
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // 2. Precio (ahora debajo del nombre)
            Text(
                text = servicio.precioCLP.toCLP(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary // Un color distintivo para el precio
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
