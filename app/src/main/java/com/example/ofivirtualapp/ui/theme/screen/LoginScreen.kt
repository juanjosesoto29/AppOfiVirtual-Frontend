package com.example.ofivirtualapp.ui.theme.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
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

@Composable
fun LoginScreen(
    onLoginOk: () -> Unit,
    onGoRegister: () -> Unit,
    authViewModel: AuthViewModel = viewModel() // Inyectamos el ViewModel
) {
    // Campos de texto con estado (pre-llenados para facilitar pruebas)
    var email by remember { mutableStateOf("test@ofivirtual.cl") }
    var password by remember { mutableStateOf("Ofivirtual1234") }

    // Obtenemos el estado de la UI desde el ViewModel y el contexto actual
    val authState = authViewModel.authUiState
    val context = LocalContext.current

    // Efecto para mostrar un mensaje (Toast) cuando hay un error
    LaunchedEffect(authState.error) {
        authState.error?.let { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            authViewModel.clearError() // Limpia el error después de mostrarlo para no repetirlo
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
            verticalArrangement = Arrangement.Center
        ) {
            Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))

            // Campo para el correo electrónico
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            // Campo para la contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))

            // Botón para ingresar
            Button(
                onClick = {
                    // Llama a la función login del ViewModel
                    authViewModel.login(email, password, onLoginOk)
                },
                enabled = !authState.isLoading, // Se deshabilita mientras está cargando
                modifier = Modifier.fillMaxWidth()
            ) {
                if (authState.isLoading) {
                    // Muestra el indicador de carga
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Ingresar")
                }
            }
            Spacer(Modifier.height(12.dp))

            // Botón para ir a la pantalla de registro
            TextButton(onClick = onGoRegister) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}
