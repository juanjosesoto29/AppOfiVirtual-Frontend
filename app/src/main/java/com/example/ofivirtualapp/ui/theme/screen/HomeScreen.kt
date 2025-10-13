package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
// Imports para los íconos del menú (TODOS INCLUIDOS)
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.SupportAgent
// Imports para los componentes de Material 3
import androidx.compose.material3.*
// Imports para el estado de Compose (remember, mutableStateOf)
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ofivirtualapp.R
import com.example.ofivirtualapp.navigation.Route // Importa TU clase Route


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    // Nuevas funciones para el menú y logout
    onGoTo: (String) -> Unit,
    onLogout: () -> Unit,

    // Funciones que ya tenías
    onRenewPlan: () -> Unit,
    onGoOficinaVirtual: () -> Unit,
    onGoContabilidad: () -> Unit,
    onGoFormalizacion: () -> Unit,
    onOpenContrato1: () -> Unit,
    onOpenContrato2: () -> Unit,
) {

    val ofiBlue = Color(0xFF071290)
    val badgeGreen = Color(0xFF34C759)
    val badgeGrey = Color(0xFF9AA3AF)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(R.drawable.ofivirtual_logo_solo),
                            contentDescription = "OfiVirtual",
                            modifier = Modifier.height(36.dp), // Tamaño actual
                            contentScale = ContentScale.Fit
                        )
                    }
                },
                actions = {
                    // Estado para controlar la visibilidad del menú
                    var menuExpanded by remember { mutableStateOf(false) }

                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Outlined.MoreVert, contentDescription = "Más opciones")
                        }

                        // Menú desplegable con las nuevas opciones
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            val handleMenuClick = { routePath: String ->
                                onGoTo(routePath)
                                menuExpanded = false
                            }

                            DropdownMenuItem(
                                text = { Text("Notificaciones") },
                                onClick = { handleMenuClick(Route.Notificaciones.path) },
                                leadingIcon = { Icon(Icons.Outlined.Notifications, null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Métodos de Pago") },
                                onClick = { handleMenuClick(Route.MetodosPago.path) },
                                leadingIcon = { Icon(Icons.Outlined.CreditCard, null) }
                            )
                            DropdownMenuItem(
                                text = { Text("FAQ") },
                                onClick = { handleMenuClick(Route.FAQ.path) },
                                leadingIcon = { Icon(Icons.Outlined.Quiz, null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Soporte") },
                                onClick = { handleMenuClick(Route.Soporte.path) },
                                leadingIcon = { Icon(Icons.Outlined.SupportAgent, null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Acerca de la Empresa") },
                                onClick = { handleMenuClick(Route.AcercaDe.path) },
                                leadingIcon = { Icon(Icons.Outlined.Info, null) }
                            )
                            Divider()
                            DropdownMenuItem(
                                text = { Text("Cerrar Sesión") },
                                onClick = {
                                    onLogout()
                                    menuExpanded = false
                                },
                                leadingIcon = { Icon(Icons.Outlined.ExitToApp, null) }
                            )
                        }
                    }
                },
                navigationIcon = { Box(Modifier.size(48.dp)) {} } // Spacer para centrar
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Inicio",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(Modifier.height(12.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Mi Plan Actual",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.weight(1f)
                        )
                        StatusChip(label = "VIGENTE", bg = badgeGreen, fg = Color.White)
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Oficina Virtual – Plan Semestral. Vence 13/12/2025",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = onRenewPlan,
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("Renovar")
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                "Accesos rápidos",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickAccessCard(
                    modifier = Modifier.weight(1f),
                    circleText = "O",
                    title = "Oficina Virtual",
                    subtitle = "Dirección tributaria",
                    circleColor = ofiBlue,
                    onClick = onGoOficinaVirtual
                )
                QuickAccessCard(
                    modifier = Modifier.weight(1f),
                    circleText = "C",
                    title = "Contabilidad",
                    subtitle = "Mensual/Laboral",
                    circleColor = ofiBlue,
                    onClick = onGoContabilidad
                )
                QuickAccessCard(
                    modifier = Modifier.weight(1f),
                    circleText = "F",
                    title = "Formalización",
                    subtitle = "Inicio de act.",
                    circleColor = ofiBlue,
                    onClick = onGoFormalizacion
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                "Mis contratos",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(Modifier.height(10.dp))

            ContractItem(
                iconLetter = "O",
                title = "Contrato Oficina Virtual",
                subtitle = "Emitido 13/12/2024",
                chipLabel = "VIGENTE",
                chipBg = badgeGreen,
                onClick = onOpenContrato1
            )
            Spacer(Modifier.height(10.dp))
            ContractItem(
                iconLetter = "C",
                title = "Contrato Contabilidad",
                subtitle = "Emitido 05/03/2025",
                chipLabel = "VENCIDO",
                chipBg = badgeGrey,
                onClick = onOpenContrato2
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

// --- INICIO DE SUB-COMPONENTES (VERSIÓN ÚNICA Y CORRECTA) ---

@Composable
private fun StatusChip(label: String, bg: Color, fg: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = fg, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuickAccessCard(
    modifier: Modifier = Modifier,
    circleText: String,
    title: String,
    subtitle: String,
    circleColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(circleColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    circleText,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContractItem(
    iconLetter: String,
    title: String,
    subtitle: String,
    chipLabel: String,
    chipBg: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    iconLetter,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            StatusChip(label = chipLabel, bg = chipBg, fg = Color.White)
        }
    }
}
