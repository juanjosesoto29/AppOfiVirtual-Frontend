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
import com.example.ofivirtualapp.data.local.storage.UserPreferences
import com.example.ofivirtualapp.viewmodel.AuthViewModel

@Composable
fun LoginScreenVm(
    vm: AuthViewModel,
    userPrefs: UserPreferences,
    onLoginOkNavigateHome: () -> Unit,
    onGoRegister: () -> Unit
) {
    val state by vm.login.collectAsStateWithLifecycle()

    LaunchedEffect(state.success) {
        if (state.success) {
            state.email.let { userPrefs.saveUserEmail(it) }
            state.userId?.let { userPrefs.saveUserId(it) }
            userPrefs.setLoggedIn(true)
            vm.clearLoginResult()
            onLoginOkNavigateHome()
        }
    }

    LoginScreen(
        email = state.email,
        pass = state.pass,
        emailError = state.emailError,
        passError = state.passError,
        canSubmit = state.canSubmit,
        isSubmitting = state.isSubmitting,
        errorMsg = state.errorMsg,
        onEmailChange = vm::onLoginEmailChange,
        onPassChange = vm::onLoginPassChange,
        onSubmit = vm::submitLogin,
        onGoRegister = onGoRegister
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreen(
    email: String,
    pass: String,
    emailError: String?,
    passError: String?,
    canSubmit: Boolean,
    isSubmitting: Boolean,
    errorMsg: String?,
    onEmailChange: (String) -> Unit,
    onPassChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onGoRegister: () -> Unit
) {
    var showPass by remember { mutableStateOf(false) }

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
                        text = "Iniciar Sesión",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ofiBlue
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(20.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = onEmailChange,
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        isError = emailError != null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    if (emailError != null) {
                        Text(
                            emailError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(top = 4.dp)
                        )
                    }

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
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    if (passError != null) {
                        Text(
                            passError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(top = 4.dp)
                        )
                    }

                    Spacer(Modifier.height(22.dp))

                    Button(
                        onClick = onSubmit,
                        enabled = canSubmit && !isSubmitting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ofiBlue)
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp),
                                color = Color.White
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Validando...", color = Color.White)
                        } else {
                            Text("Entrar", color = Color.White)
                        }
                    }

                    if (errorMsg != null) {
                        Spacer(Modifier.height(10.dp))
                        Text(
                            errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(Modifier.height(18.dp))

                    OutlinedButton(
                        onClick = onGoRegister,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = ofiBlue
                        ),
                        border = BorderStroke(1.3.dp, ofiBlue)
                    ) {
                        Text("Crear cuenta", fontWeight = FontWeight.Medium)
                    }

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        color = gold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
