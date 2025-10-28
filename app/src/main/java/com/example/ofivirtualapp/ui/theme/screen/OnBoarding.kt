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

@Composable
fun OnboardingScreen(
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onSkip: () -> Unit
) {
    val OfiBlue = Color(0xFF071290)

    var start by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { start = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OfiBlue)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // LOGO
            AnimatedVisibility(
                visible = start,
                enter = fadeIn(animationSpec = tween(450)) +
                        slideInVertically(initialOffsetY = { it / 4 }, animationSpec = tween(450))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ofivirtual_logo),
                    contentDescription = "Logo OfiVirtual",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 10.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // TÍTULO
            AnimatedVisibility(
                visible = start,
                enter = fadeIn(animationSpec = tween(500, delayMillis = 120)) +
                        slideInVertically(initialOffsetY = { it / 5 }, animationSpec = tween(500, delayMillis = 120))
            ) {
                Text(
                    text = "Bienvenido a OfiVirtual",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(8.dp))

            // SUBTÍTULO
            AnimatedVisibility(
                visible = start,
                enter = fadeIn(animationSpec = tween(500, delayMillis = 220)) +
                        slideInVertically(initialOffsetY = { it / 6 }, animationSpec = tween(500, delayMillis = 220))
            ) {
                Text(
                    text = "Tu oficina virtual, contabilidad y formalización en un solo lugar.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(40.dp))


            AnimatedVisibility(
                visible = start,
                enter = fadeIn(animationSpec = tween(500, delayMillis = 320)) +
                        slideInVertically(initialOffsetY = { it / 6 }, animationSpec = tween(500, delayMillis = 320))
            ) {
                val gradient = Brush.horizontalGradient(
                    listOf(Color.White, Color(0xFFF2F5FF)) // blanco -> blanco azulado suave
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(gradient),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = onRegister,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = OfiBlue
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("Comenzar", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            AnimatedVisibility(
                visible = start,
                enter = fadeIn(animationSpec = tween(500, delayMillis = 420)) +
                        slideInVertically(initialOffsetY = { it / 8 }, animationSpec = tween(500, delayMillis = 420))
            ) {
                OutlinedButton(
                    onClick = onLogin,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = BorderStroke(1.dp, Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Ya tengo cuenta", fontSize = 16.sp)
                }
            }

            Spacer(Modifier.height(8.dp))

            AnimatedVisibility(
                visible = start,
                enter = fadeIn(animationSpec = tween(500, delayMillis = 480)) +
                        slideInVertically(initialOffsetY = { it / 10 }, animationSpec = tween(500, delayMillis = 480))
            ) {
                TextButton(onClick = onSkip) {
                    Text(
                        "Entrar como invitado",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
