package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OficinaVirtualScreen(
    onAddToCart: (PlanOV) -> Unit = {}
) {
    val OfiBlue = Color(0xFF071290)

    val planes = listOf(
        PlanOV(
            nombre = "Plan Semestral",
            precioCLP = 86_000,
            duracionMeses = 6,
            bullets = listOf(
                "Incluye domicilio tributario.",
                "Recepción de correspondencia."
            )
        ),
        PlanOV(
            nombre = "Plan Anual",
            precioCLP = 126_000,
            duracionMeses = 12,
            bullets = listOf(
                "Incluye domicilio tributario.",
                "Recepción de correspondencia.",
                "Precio más conveniente."
            )
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Oficina Virtual",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(Modifier.height(12.dp))

        planes.forEachIndexed { idx, plan ->
            PlanOVCard(
                plan = plan,
                buttonColor = OfiBlue,
                onAddToCart = { onAddToCart(plan) }
            )
            if (idx != planes.lastIndex) Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.height(12.dp))
    }
}

/* ----------------- Model & UI ----------------- */

data class PlanOV(
    val nombre: String,
    val precioCLP: Int,
    val duracionMeses: Int,
    val bullets: List<String>
)

@Composable
private fun PlanOVCard(
    plan: PlanOV,
    buttonColor: Color,
    onAddToCart: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            // --- CAMBIO DE DISEÑO APLICADO AQUÍ ---

            // 1. Nombre del plan
            Text(
                text = plan.nombre,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // 2. Precio + duración (debajo del nombre)
            Text(
                text = "${plan.precioCLP.toCLP()} (${plan.duracionMeses} meses)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            // 3. Bullets
            Spacer(Modifier.height(8.dp))
            plan.bullets.forEach {
                Text(
                    text = "• $it",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 4. Botón
            Spacer(Modifier.height(14.dp))
            Button(
                onClick = onAddToCart,
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor, contentColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Agregar al carrito", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

/* ----------------- Utils ----------------- */

private fun Int.toCLP(): String {
    if (this == 0) return "$0"
    val s = this.toString()
    val sb = StringBuilder()
    var count = 0
    for (i in s.length - 1 downTo 0) {
        sb.append(s[i])
        count++
        if (count == 3 && i != 0) {
            sb.append('.')
            count = 0
        }
    }
    return "$" + sb.reverse().toString()
}
