package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ofivirtualapp.R
import com.example.ofivirtualapp.ui.theme.OfiBlue
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var start by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        start = true
        // Aquí podrías hacer un chequeo real (token/usuario) en ~600–1200ms
        delay(900)
        onFinished()
    }

    val gradient = Brush.verticalGradient(
        listOf(
            OfiBlue.copy(alpha = 0.98f),
            OfiBlue.copy(alpha = 0.88f),
            OfiBlue.copy(alpha = 0.92f)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = start,
            enter = fadeIn(animationSpec = tween(500)) +
                    scaleIn(initialScale = 0.92f, animationSpec = tween(500))
        ) {
            Image(
                painter = painterResource(id = R.drawable.ofivirtual_logo),
                contentDescription = "Logo OfiVirtual",
                modifier = Modifier.size(180.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}
