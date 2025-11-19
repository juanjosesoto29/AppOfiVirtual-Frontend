package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ofivirtualapp.data.remote.ticket.TicketResponse
import com.example.ofivirtualapp.viewmodel.TicketViewModel

// ---------------------------
//    WRAPPER
// ---------------------------
@Composable
fun SoporteScreenVm(
    vm: TicketViewModel,
    onNavigateBack: () -> Unit,
    onGoToFaq: () -> Unit
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { vm.loadTickets() }

    SoporteScreen(
        isLoading = state.isLoading,
        tickets = state.tickets,
        errorMsg = state.errorMsg,
        successMsg = state.successMsg,
        onCreateTicket = { asunto, descripcion ->
            vm.createTicket(
                userId = 1L,   // reemplazar por user real luego
                empresaId = null,
                asunto = asunto,
                descripcion = descripcion
            )
        },
        onDeleteTicket = { vm.deleteTicket(it) },
        onUpdateTicket = { ticket, newAsunto, newDescripcion ->
            vm.updateTicket(
                id = ticket.id,
                userId = ticket.userId,
                empresaId = ticket.empresaId,
                asunto = newAsunto,
                descripcion = newDescripcion
            )
        },
        onClearMessages = { vm.clearMessages() },
        onNavigateBack = onNavigateBack,
        onGoToFaq = onGoToFaq
    )
}

// ---------------------------
//    MAIN SCREEN
// ---------------------------
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun SoporteScreen(
    isLoading: Boolean,
    tickets: List<TicketResponse>,
    errorMsg: String?,
    successMsg: String?,
    onCreateTicket: (String, String) -> Unit,
    onDeleteTicket: (Long) -> Unit,
    onUpdateTicket: (TicketResponse, String, String) -> Unit,
    onClearMessages: () -> Unit,
    onNavigateBack: () -> Unit,
    onGoToFaq: () -> Unit
) {
    var asunto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var ticketToEdit by remember { mutableStateOf<TicketResponse?>(null) }

    val isFormValid = asunto.isNotBlank() && descripcion.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Soporte y Tickets") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onGoToFaq) {
                        Icon(Icons.Filled.HelpOutline, contentDescription = "Preguntas frecuentes")
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // -------------------------------
            //     MENSAJES (SUCCESS / ERROR)
            // -------------------------------
            AnimatedVisibility(
                visible = errorMsg != null || successMsg != null,
                enter = fadeIn(tween(250)) + slideInVertically(initialOffsetY = { -it / 2 }),
                exit = fadeOut(tween(200)) + slideOutVertically(targetOffsetY = { -it / 2 })
            ) {
                if (errorMsg != null) {
                    AssistChip(
                        onClick = onClearMessages,
                        label = { Text(errorMsg) },
                        colors = AssistChipDefaults.assistChipColors(
                            labelColor = MaterialTheme.colorScheme.error,
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                } else if (successMsg != null) {
                    AssistChip(
                        onClick = onClearMessages,
                        label = { Text(successMsg) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // -------------------------------
            //     FORMULARIO NUEVO TICKET
            // -------------------------------
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(tween(250)),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = "Crear nuevo ticket",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    OutlinedTextField(
                        value = asunto,
                        onValueChange = { asunto = it },
                        label = { Text("Asunto") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción del problema") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(100.dp),
                        maxLines = 5
                    )

                    Button(
                        onClick = {
                            onCreateTicket(asunto.trim(), descripcion.trim())
                            asunto = ""
                            descripcion = ""
                        },
                        enabled = isFormValid && !isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AnimatedContent(targetState = isLoading) { loading ->
                            if (loading) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CircularProgressIndicator(
                                        strokeWidth = 2.dp,
                                        modifier = Modifier
                                            .size(18.dp)
                                            .padding(end = 8.dp)
                                    )
                                    Text("Enviando…")
                                }
                            } else {
                                Text("Enviar ticket")
                            }
                        }
                    }
                }
            }

            // -------------------------------
            //         LISTA DE TICKETS
            // -------------------------------
            Text(
                text = "Mis tickets",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )

            AnimatedContent(
                targetState = Triple(isLoading, tickets.isEmpty(), tickets),
                label = "tickets_anim"
            ) { (loading, empty, list) ->

                when {
                    loading && empty -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    empty -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Aún no has enviado tickets.")
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(list, key = { it.id }) { ticket ->
                                TicketCard(
                                    ticket = ticket,
                                    onDelete = { onDeleteTicket(ticket.id) },
                                    onEdit = { ticketToEdit = ticket }
                                )
                            }
                        }
                    }
                }
            }
        }

        // -------------------------------
        //         DIÁLOGO EDITAR
        // -------------------------------
        ticketToEdit?.let { ticket ->
            EditTicketDialog(
                ticket = ticket,
                onDismiss = { ticketToEdit = null },
                onUpdate = { newAsunto, newDescripcion ->
                    onUpdateTicket(ticket, newAsunto, newDescripcion)
                    ticketToEdit = null
                }
            )
        }
    }
}

// ---------------------------
//     CARD EXPANDIBLE
// ---------------------------
@Composable
private fun TicketCard(
    ticket: TicketResponse,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(tween(300))
            .clickable { expanded = !expanded },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = ticket.asunto,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar")
                }

                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                }
            }

            Text(
                text = "Estado: ${ticket.estado}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(8.dp))

            // Descripción expandible / colapsable
            Text(
                text = ticket.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = if (expanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Creado: ${ticket.fechaCreacion}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            AnimatedVisibility(visible = expanded) {
                Text(
                    "Tocar para contraer ▲",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 6.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            AnimatedVisibility(visible = !expanded) {
                Text(
                    "Tocar para expandir ▼",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 6.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ---------------------------
//     DIÁLOGO EDITAR
// ---------------------------
@Composable
private fun EditTicketDialog(
    ticket: TicketResponse,
    onDismiss: () -> Unit,
    onUpdate: (String, String) -> Unit
) {
    var asunto by remember { mutableStateOf(ticket.asunto) }
    var descripcion by remember { mutableStateOf(ticket.descripcion) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar ticket") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = asunto,
                    onValueChange = { asunto = it },
                    label = { Text("Asunto") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción del problema") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            Button(onClick = { onUpdate(asunto.trim(), descripcion.trim()) }) {
                Text("Guardar cambios")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
