package com.example.ofivirtualapp.ui.theme.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ofivirtualapp.viewmodel.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegistered: () -> Unit,
    onGoLogin: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val authState = authViewModel.authUiState
    val context = LocalContext.current

    // --- Lógica para el DatePickerDialog ---
    var showDatePicker by remember { mutableStateOf(false) }

    // -->> CORRECCIÓN APLICADA AQUÍ <<--
    // El estado del DatePicker DEBE ser recordado aquí, fuera del diálogo.
    // Esto evita que se reinicie cada vez que el diálogo aparece.
    val datePickerState = rememberDatePickerState()

    // Este LaunchedEffect ahora funcionará correctamente porque `datePickerState` persiste.
    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            birthDate = formatter.format(Date(it))
        }
    }

    // El resto del código no cambia...
    LaunchedEffect(authState) {
        authState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            authViewModel.clearError()
        }
        if (authState.registerSuccess) {
            Toast.makeText(context, "¡Registro exitoso! Por favor, inicia sesión.", Toast.LENGTH_LONG).show()
            authViewModel.resetRegisterStatus()
            onRegistered()
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            // El diálogo ahora usa el estado que persiste fuera de él.
            DatePicker(state = datePickerState)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = birthDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha de nacimiento") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Seleccionar fecha"
                    )
                }
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = password != confirmPassword,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    authViewModel.register(email, password, name)
                },
                enabled = !authState.isLoading &&
                        password == confirmPassword &&
                        email.isNotBlank() &&
                        password.isNotBlank() &&
                        name.isNotBlank() &&
                        birthDate.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (authState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Registrarme")
                }
            }
            Spacer(Modifier.height(12.dp))

            TextButton(onClick = onGoLogin) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}
