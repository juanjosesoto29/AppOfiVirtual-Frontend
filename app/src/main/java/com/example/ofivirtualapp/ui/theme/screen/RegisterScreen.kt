package com.example.ofivirtualapp.ui.theme.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ofivirtualapp.domain.* // Importamos tus validadores
import com.example.ofivirtualapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegistered: () -> Unit,
    onGoLogin: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    // --- ESTADOS DE LOS CAMPOS ---
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // --- ESTADOS DE VALIDACIÓN ---
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    // --- LÓGICA DEL PREFIJO TELEFÓNICO ---
    val countryCodes = listOf("+56", "+51", "+54", "+57", "+52") // Chile, Perú, Argentina, Colombia, México
    var selectedPrefix by remember { mutableStateOf(countryCodes[0]) } // +56 por defecto
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val authState = authViewModel.authUiState
    val context = LocalContext.current

    // Efecto para manejar la respuesta del ViewModel (éxito o error)
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

    // --- FUNCIÓN DE VALIDACIÓN ---
    fun validateFields(): Boolean {
        nameError = validateNameLettersOnly(name)
        emailError = validateEmail(email)
        phoneError = validatePhoneDigitsOnly(phone)
        passwordError = validateStrongPass(password)
        confirmPasswordError = validateConfirm(password, confirmPassword)

        return nameError == null && emailError == null && phoneError == null &&
                passwordError == null && confirmPasswordError == null
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

            // --- CAMPO NOMBRE ---
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; nameError = null },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                isError = nameError != null,
                supportingText = { if (nameError != null) Text(nameError!!) }
            )
            Spacer(Modifier.height(4.dp)) // Reducimos espacio

            // --- CAMPO TELÉFONO CON PREFIJO ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Dropdown para prefijos
                ExposedDropdownMenuBox(
                    expanded = isDropdownExpanded,
                    onExpandedChange = { isDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedPrefix,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor() // Ancla el menú a este campo
                            .width(120.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false }
                    ) {
                        countryCodes.forEach { code ->
                            DropdownMenuItem(
                                text = { Text(code) },
                                onClick = {
                                    selectedPrefix = code
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.width(8.dp))

                // Campo para el número
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it; phoneError = null },
                    label = { Text("Teléfono") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = phoneError != null,
                    supportingText = { if (phoneError != null) Text(phoneError!!) }
                )
            }
            Spacer(Modifier.height(4.dp))

            // --- CAMPO EMAIL ---
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; emailError = null },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = emailError != null,
                supportingText = { if (emailError != null) Text(emailError!!) }
            )
            Spacer(Modifier.height(4.dp))

            // --- CAMPO CONTRASEÑA ---
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; passwordError = null },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError != null,
                supportingText = { if (passwordError != null) Text(passwordError!!) }
            )
            Spacer(Modifier.height(4.dp))

            // --- CAMPO CONFIRMAR CONTRASEÑA ---
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; confirmPasswordError = null },
                label = { Text("Confirmar contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = confirmPasswordError != null,
                supportingText = { if (confirmPasswordError != null) Text(confirmPasswordError!!) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))

            // --- BOTÓN DE REGISTRO ---
            Button(
                onClick = {
                    if (validateFields()) {
                        val fullPhoneNumber = selectedPrefix + phone
                        authViewModel.register(email, password, name, fullPhoneNumber)
                    }
                },
                enabled = !authState.isLoading,
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
