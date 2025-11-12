package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ofivirtualapp.R
import com.example.ofivirtualapp.ui.theme.Gold
import com.example.ofivirtualapp.ui.theme.OfiBlue

@Composable
fun OnboardingScreen(
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onSkip: () -> Unit
) {
    var start by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { start = true }

    // Degradado azul limpio (dos tonos)
    val bg = Brush.verticalGradient(
        listOf(
            Color(0xFF0A1BA6),
            Color(0xFF1426D3)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
            .navigationBarsPadding()
            .padding(horizontal = 22.dp),
        contentAlignment = Alignment.Center
    ) {
        // Layout sin scroll: usamos pesos y tamaños adaptativos
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val h = maxHeight
            val logoSize = if (h < 640.dp) 120.dp else 160.dp       // se ajusta en pantallas pequeñas
            val cardPadV = if (h < 640.dp) 14.dp else 18.dp
            val cardRadius = 22.dp

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))

                // Logo + marca
                AnimatedVisibility(
                    visible = start,
                    enter = fadeIn(tween(420)) +
                            slideInVertically(initialOffsetY = { it / 6 }, animationSpec = tween(420))
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.ofivirtual_logo),
                            contentDescription = "Logo OfiVirtual",
                            modifier = Modifier
                                .size(200.dp)
                                .padding(bottom = 8.dp),
                            contentScale = ContentScale.Fit
                        )

                    }
                }

                Spacer(Modifier.height(if (h < 640.dp) 12.dp else 18.dp))

                // Tarjeta de texto (glass muy sutil)
                AnimatedVisibility(
                    visible = start,
                    enter = fadeIn(tween(500, delayMillis = 120)) +
                            slideInVertically(initialOffsetY = { it / 7 }, animationSpec = tween(500, delayMillis = 120))
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.10f),
                        shape = RoundedCornerShape(cardRadius),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = cardPadV),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Bienvenido a OfiVirtual",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Tu oficina virtual, contabilidad y formalización en un solo lugar.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White.copy(alpha = 0.92f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Empuja el bloque de botones hacia abajo pero sin salir de pantalla
                Spacer(Modifier.weight(1f))

                // Botón principal: relleno de un solo color (dorado corporativo)
                AnimatedVisibility(
                    visible = start,
                    enter = fadeIn(tween(500, delayMillis = 240)) +
                            slideInVertically(initialOffsetY = { it / 8 }, animationSpec = tween(500, delayMillis = 240))
                ) {
                    Button(
                        onClick = onRegister,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Gold,          // ✅ sólido
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Text("Comenzar", style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp))
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Botón secundario (outline suave, radio grande)
                AnimatedVisibility(
                    visible = start,
                    enter = fadeIn(tween(500, delayMillis = 320)) +
                            slideInVertically(initialOffsetY = { it / 10 }, animationSpec = tween(500, delayMillis = 320))
                ) {
                    OutlinedButton(
                        onClick = onLogin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = BorderStroke(1.2.dp, Color.White.copy(alpha = 0.95f))
                    ) {
                        Text("Ya tengo cuenta", style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp))
                    }
                }

                Spacer(Modifier.height(10.dp))

                // Link invitado (siempre visible por el padding de barras de navegación)
                AnimatedVisibility(
                    visible = start,
                    enter = fadeIn(tween(500, delayMillis = 380)) +
                            slideInVertically(initialOffsetY = { it / 12 }, animationSpec = tween(500, delayMillis = 380))
                ) {
                    TextButton(onClick = onSkip) {
                        Text(
                            "Entrar como invitado",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.88f)
                        )
                    }
                }

                Spacer(Modifier.height(if (h < 640.dp) 8.dp else 14.dp))
            }
        }
    }
}
