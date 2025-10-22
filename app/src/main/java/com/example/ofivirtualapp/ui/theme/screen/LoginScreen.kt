package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ofivirtualapp.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel, // <-- PARÁMETRO AÑADIDO Y REORDENADO
    onLoginOk: () -> Unit,
    onGoRegister: () -> Unit
) {
    val formState = authViewModel.loginFormState // Ahora esto es seguro

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bienvenido de Vuelta", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Inicia sesión para continuar.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Spacer(Modifier.height(32.dp))

        formState.generalError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        OutlinedTextField(
            value = formState.email,
            onValueChange = authViewModel::onLoginEmailChange,
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth(),
            isError = formState.emailError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )
        formState.emailError?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = formState.password,
            onValueChange = authViewModel::onLoginPasswordChange,
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            isError = formState.passwordError != null,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )
        formState.passwordError?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(Modifier.height(8.dp))

        Text(
            text = "¿Olvidaste tu contraseña?",
            modifier = Modifier
                .align(Alignment.End)
                .clickable { /* TODO: Navegar a pantalla de recuperación */ },
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { authViewModel.login(onSuccess = onLoginOk) },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Iniciar Sesión")
        }
        Spacer(Modifier.height(16.dp))

        Row {
            Text("¿No tienes una cuenta?", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.width(4.dp))
            Text(
                text = "Regístrate",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.clickable(onClick = onGoRegister)
            )
        }
    }
}