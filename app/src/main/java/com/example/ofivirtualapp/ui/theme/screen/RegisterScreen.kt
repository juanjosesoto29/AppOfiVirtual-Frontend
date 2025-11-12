package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ofivirtualapp.viewmodel.AuthViewModel

@Composable
fun RegisterScreenVm(
    vm: AuthViewModel,
    onRegisteredNavigateLogin: () -> Unit,
    onGoLogin: () -> Unit
) {
    val state by vm.register.collectAsStateWithLifecycle()

    LaunchedEffect(state.success) {
        if (state.success) {
            vm.clearRegisterResult()
            onRegisteredNavigateLogin()
        }
    }

    RegisterScreen(
        name = state.name,
        email = state.email,
        phone = state.phone,
        pass = state.pass,
        confirm = state.confirm,
        nameError = state.nameError,
        emailError = state.emailError,
        phoneError = state.phoneError,
        passError = state.passError,
        confirmError = state.confirmError,
        canSubmit = state.canSubmit,
        isSubmitting = state.isSubmitting,
        errorMsg = state.errorMsg,
        onNameChange = vm::onRegisterNameChange,
        onEmailChange = vm::onRegisterEmailChange,
        onPhoneChange = vm::onRegisterPhoneChange,
        onPassChange = vm::onRegisterPassChange,
        onConfirmChange = vm::onRegisterConfirmChange,
        onSubmit = vm::submitRegister,
        onGoLogin = onGoLogin
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterScreen(
    name: String,
    email: String,
    phone: String,
    pass: String,
    confirm: String,
    nameError: String?,
    emailError: String?,
    phoneError: String?,
    passError: String?,
    confirmError: String?,
    canSubmit: Boolean,
    isSubmitting: Boolean,
    errorMsg: String?,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPassChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onGoLogin: () -> Unit
) {
    var showPass by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }

    val ofiBlue = Color(0xFF071290)
    val gold = Color(0xFFDAA520)

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(ofiBlue.copy(alpha = 0.95f), Color(0xFFF5F7FF))
                    )
                )
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Crear cuenta",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ofiBlue
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(20.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = onNameChange,
                        label = { Text("Nombre completo") },
                        singleLine = true,
                        isError = nameError != null,
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    if (nameError != null)
                        Text(nameError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)

                    Spacer(Modifier.height(10.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = onEmailChange,
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = emailError != null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    if (emailError != null)
                        Text(emailError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)

                    Spacer(Modifier.height(10.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = onPhoneChange,
                        label = { Text("Teléfono") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = phoneError != null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    if (phoneError != null)
                        Text(phoneError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)

                    Spacer(Modifier.height(10.dp))

                    OutlinedTextField(
                        value = pass,
                        onValueChange = onPassChange,
                        label = { Text("Contraseña") },
                        singleLine = true,
                        visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPass = !showPass }) {
                                Icon(
                                    imageVector = if (showPass) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (showPass) "Ocultar contraseña" else "Mostrar contraseña",
                                    tint = ofiBlue
                                )
                            }
                        },
                        isError = passError != null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    if (passError != null)
                        Text(passError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)

                    Spacer(Modifier.height(10.dp))

                    OutlinedTextField(
                        value = confirm,
                        onValueChange = onConfirmChange,
                        label = { Text("Confirmar contraseña") },
                        singleLine = true,
                        visualTransformation = if (showConfirm) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showConfirm = !showConfirm }) {
                                Icon(
                                    imageVector = if (showConfirm) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (showConfirm) "Ocultar contraseña" else "Mostrar contraseña",
                                    tint = ofiBlue
                                )
                            }
                        },
                        isError = confirmError != null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    if (confirmError != null)
                        Text(confirmError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)

                    Spacer(Modifier.height(22.dp))

                    Button(
                        onClick = onSubmit,
                        enabled = canSubmit && !isSubmitting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = gold)
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp),
                                color = Color.White
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Creando cuenta...", color = Color.White)
                        } else {
                            Text("Registrarme", color = Color.White)
                        }
                    }

                    if (errorMsg != null) {
                        Spacer(Modifier.height(10.dp))
                        Text(errorMsg, color = MaterialTheme.colorScheme.error)
                    }

                    Spacer(Modifier.height(18.dp))

                    OutlinedButton(
                        onClick = onGoLogin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = ofiBlue
                        ),
                        border = BorderStroke(1.3.dp, ofiBlue)
                    ) {
                        Text("Ya tengo una cuenta", fontWeight = FontWeight.Medium)
                    }

                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Al registrarte, aceptas nuestros Términos y Condiciones.",
                        fontSize = 12.sp,
                        color = ofiBlue.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
