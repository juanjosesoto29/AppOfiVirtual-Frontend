package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ofivirtualapp.R
import com.example.ofivirtualapp.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onGoTo: (String) -> Unit,
    onLogout: () -> Unit,
    onRenewPlan: () -> Unit,
    onGoOficinaVirtual: () -> Unit,
    onGoContabilidad: () -> Unit,
    onGoFormalizacion: () -> Unit,
    onOpenContrato1: (String) -> Unit,
    onOpenContrato2: (String) -> Unit,
) {
    val ofiBlue = Color(0xFF071290)
    val badgeGreen = Color(0xFF34C759)
    val badgeGrey = Color(0xFF9AA3AF)
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(R.drawable.ofivirtual_logo_solo),
                            contentDescription = "OfiVirtual",
                            modifier = Modifier.height(36.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                },
                actions = {
                    var menuExpanded by remember { mutableStateOf(false) }

                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Outlined.MoreVert, contentDescription = "Más opciones")
                        }

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
                            HorizontalDivider(Modifier.padding(vertical = 4.dp))
                            DropdownMenuItem(
                                text = { Text("Cerrar Sesión", color = Color.Red) },
                                onClick = {
                                    onLogout()
                                    menuExpanded = false
                                },
                                leadingIcon = {
                                    Icon(Icons.Outlined.ExitToApp, tint = Color.Red, contentDescription = null)
                                }
                            )
                        }
                    }
                },
                navigationIcon = { Box(Modifier.size(48.dp)) {} }
            )
        }
    ) { innerPadding ->

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFF9FAFB), Color(0xFFEFF2FF))
                    )
                )
                .padding(innerPadding)
        ) {
            val screenHeight = maxHeight
            val spacing = if (screenHeight < 650.dp) 10.dp else 18.dp
            val textScale = if (screenHeight < 650.dp) 0.9f else 1f
            val cardPadding = if (screenHeight < 650.dp) 10.dp else 14.dp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = spacing)
            ) {
                // --- Header ---
                Text(
                    text = "Inicio",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = (22.sp * textScale)
                    ),
                    color = ofiBlue
                )

                Spacer(Modifier.height(spacing))

                // --- Plan actual ---
                Card(
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(Modifier.padding(cardPadding)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Mi Plan Actual",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                modifier = Modifier.weight(1f),
                                fontSize = (15.sp * textScale)
                            )
                            StatusChip("VIGENTE", badgeGreen, Color.White)
                        }
                        Text(
                            "Oficina Virtual – Plan Semestral. Vence 13/12/2025",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = (13.sp * textScale)
                        )
                        Spacer(Modifier.height(spacing / 2))
                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = onRenewPlan,
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ofiBlue)
                            ) {
                                Text("Renovar", color = Color.White, fontSize = (13.sp * textScale))
                            }
                        }
                    }
                }

                Spacer(Modifier.height(spacing * 1.4f))

                // --- Accesos rápidos ---
                Text(
                    "Accesos rápidos",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    fontSize = (15.sp * textScale)
                )
                Spacer(Modifier.height(spacing / 2))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickAccessCard(
                        modifier = Modifier.weight(1f),
                        circleText = "O",
                        title = "Oficina Virtual",
                        subtitle = "Dirección",
                        circleColor = ofiBlue,
                        onClick = onGoOficinaVirtual
                    )
                    QuickAccessCard(
                        modifier = Modifier.weight(1f),
                        circleText = "C",
                        title = "Contabilidad",
                        subtitle = "Mensual",
                        circleColor = ofiBlue,
                        onClick = onGoContabilidad
                    )
                    QuickAccessCard(
                        modifier = Modifier.weight(1f),
                        circleText = "F",
                        title = "Formalización",
                        subtitle = "Inicio act.",
                        circleColor = ofiBlue,
                        onClick = onGoFormalizacion
                    )
                }

                Spacer(Modifier.height(spacing * 1.6f))

                // --- Contratos ---
                Text(
                    "Mis contratos",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    fontSize = (15.sp * textScale)
                )
                Spacer(Modifier.height(spacing / 2))

                ContractItem(
                    iconLetter = "O",
                    title = "Contrato Oficina Virtual",
                    subtitle = "Emitido 13/12/2024",
                    chipLabel = "VIGENTE",
                    chipBg = badgeGreen,
                    onClick = { onOpenContrato1("https://drive.google.com/file/d/1U2JMb9S1laCQOtB8c9JCX0pNOb5TrzFl/view?usp=sharing") }
                )

                Spacer(Modifier.height(spacing))

                ContractItem(
                    iconLetter = "C",
                    title = "Contrato Contabilidad",
                    subtitle = "Emitido 05/03/2025",
                    chipLabel = "VENCIDO",
                    chipBg = badgeGrey,
                    onClick = { onOpenContrato2("https://drive.google.com/file/d/1DpVk6lXWrnLHAPf8tBAEHQMkWsZqhnhJ/view?usp=sharing") }
                )

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun StatusChip(label: String, bg: Color, fg: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = fg, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
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
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(circleColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    circleText,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
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
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(iconLetter, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            StatusChip(label = chipLabel, bg = chipBg, fg = Color.White)
        }
    }
}
