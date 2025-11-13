package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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

// VM wrapper
@Composable
fun SoporteScreenVm(
    vm: TicketViewModel,
    onNavigateBack: () -> Unit,
    onGoToFaq: () -> Unit
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    // Cargar tickets al entrar
    LaunchedEffect(Unit) {
        vm.loadTickets()
    }

    SoporteScreen(
        isLoading = state.isLoading,
        tickets = state.tickets,
        errorMsg = state.errorMsg,
        successMsg = state.successMsg,
        onCreateTicket = { asunto, descripcion ->
            // TODO: reemplazar 1L por el userId real cuando lo tengas
            vm.createTicket(
                userId = 1L,
                empresaId = null,
                asunto = asunto,
                descripcion = descripcion
            )
        },
        onDeleteTicket = { vm.deleteTicket(it) },
        onClearMessages = { vm.clearMessages() },
        onNavigateBack = onNavigateBack,
        onGoToFaq = onGoToFaq
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SoporteScreen(
    isLoading: Boolean,
    tickets: List<TicketResponse>,
    errorMsg: String?,
    successMsg: String?,
    onCreateTicket: (String, String) -> Unit,
    onDeleteTicket: (Long) -> Unit,
    onClearMessages: () -> Unit,
    onNavigateBack: () -> Unit,
    onGoToFaq: () -> Unit
) {
    var asunto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

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
                .padding(16.dp)
        ) {

            // Mensajes de error/éxito
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
                Spacer(Modifier.height(12.dp))
            } else if (successMsg != null) {
                AssistChip(
                    onClick = onClearMessages,
                    label = { Text(successMsg) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
            }

            // Formulario para nuevo ticket
            Text(
                text = "Crear nuevo ticket",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = asunto,
                onValueChange = { asunto = it },
                label = { Text("Asunto") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción del problema") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                maxLines = 5
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    onCreateTicket(asunto.trim(), descripcion.trim())
                    asunto = ""
                    descripcion = ""
                },
                enabled = isFormValid && !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 8.dp)
                    )
                    Text("Enviando...")
                } else {
                    Text("Enviar ticket")
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Mis tickets",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(Modifier.height(8.dp))

            if (isLoading && tickets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (tickets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Aún no has enviado tickets de soporte.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(tickets) { ticket ->
                        TicketCard(
                            ticket = ticket,
                            onDelete = { onDeleteTicket(ticket.id) }
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TicketCard(
    ticket: TicketResponse,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = ticket.asunto,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar ticket")
                }
            }

            Text(
                text = "Estado: ${ticket.estado}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = ticket.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Creado: ${ticket.fechaCreacion}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
