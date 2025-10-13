package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PasswordRecoveryScreen(
    onBack: () -> Unit,
    onRecovered: () -> Unit
) {
    Column(Modifier.padding(16.dp)) {
        Text("Recuperar contraseña")
        Spacer(Modifier.height(12.dp))
        // Aquí iría un TextField para ingresar el correo
        Button(onClick = onRecovered) { Text("Enviar enlace") }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onBack) { Text("Volver") }
    }
}