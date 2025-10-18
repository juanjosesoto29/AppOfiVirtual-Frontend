package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.ofivirtualapp.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onRegistered: () -> Unit,
    onGoLogin: () -> Unit
) {
    val formState = authViewModel.registerFormState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))
        Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Ingresa tus datos para comenzar.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = formState.name,
            // 🔹 CORRECCIÓN: Usamos la función correcta del ViewModel 🔹
            onValueChange = { authViewModel.onRegisterNameChange(it) },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth(),
            isError = formState.nameError != null,
            singleLine = true
        )
        formState.nameError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = formState.email,
            // 🔹 CORRECCIÓN: Usamos la función correcta del ViewModel 🔹
            onValueChange = { authViewModel.onRegisterEmailChange(it) },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth(),
            isError = formState.emailError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )
        formState.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
        Spacer(Modifier.height(16.dp))

        PhoneTextField(
            phonePrefix = formState.phonePrefix,
            phoneNumber = formState.phoneNumber,
            // 🔹 CORRECCIÓN: Usamos las funciones correctas del ViewModel 🔹
            onPrefixChange = { authViewModel.onRegisterPhonePrefixChange(it) },
            onNumberChange = { authViewModel.onRegisterPhoneNumberChange(it) },
            error = formState.phoneError
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = formState.password,
            // 🔹 CORRECCIÓN: Usamos la función correcta del ViewModel 🔹
            onValueChange = { authViewModel.onRegisterPasswordChange(it) },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            isError = formState.passwordError != null,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )
        formState.passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = formState.confirmPassword,
            // 🔹 CORRECCIÓN: Usamos la función correcta del ViewModel 🔹
            onValueChange = { authViewModel.onRegisterConfirmPasswordChange(it) },
            label = { Text("Confirmar Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            isError = formState.confirmPasswordError != null,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )
        formState.confirmPasswordError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
        Spacer(Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Checkbox(
                checked = formState.termsAccepted,
                // 🔹 CORRECCIÓN: Usamos la función correcta del ViewModel 🔹
                onCheckedChange = { authViewModel.onRegisterTermsChange(it) }
            )
            Text(
                text = buildAnnotatedString {
                    append("Acepto los ")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                        append("Términos y Condiciones")
                    }
                },
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.clickable { /* Navegar a términos */ }
            )
        }
        formState.termsError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.fillMaxWidth()) }
        Spacer(Modifier.height(24.dp))

        Button(
            // 🔹 CORRECCIÓN FINAL: El botón es el ÚNICO que llama a register() 🔹
            onClick = { authViewModel.register(onSuccess = onRegistered) },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Registrarse")
        }
        Spacer(Modifier.height(16.dp))

        Row {
            Text("¿Ya tienes una cuenta?", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.width(4.dp))
            Text(
                text = "Inicia Sesión",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.clickable(onClick = onGoLogin)
            )
        }
        Spacer(Modifier.height(32.dp))
    }
}

// PhoneTextField no necesita cambios
@Composable
fun PhoneTextField(
    phonePrefix: String,
    phoneNumber: String,
    onPrefixChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    error: String?
) {
    var expanded by remember { mutableStateOf(false) }
    val countryCodes = listOf("+56", "+51", "+54", "+57", "+1")

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = onNumberChange,
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            isError = error != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            leadingIcon = {
                Row(
                    modifier = Modifier
                        .clickable { expanded = true }
                        .padding(start = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(phonePrefix)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Seleccionar prefijo")

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        countryCodes.forEach { code ->
                            DropdownMenuItem(
                                text = { Text(code) },
                                onClick = {
                                    onPrefixChange(code)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        )
        error?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
    }
}
