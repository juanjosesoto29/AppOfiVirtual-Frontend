package com.example.ofivirtualapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// ——— Paleta extendida (corporativa elegante) ———
private val Neutral900 = Color(0xFF0B1220)
private val Neutral700 = Color(0xFF2C3444)
private val Neutral100 = Color(0xFFE9EDF3)
private val SurfaceLight = Color(0xFFF7F9FC)
private val SurfaceDark = Color(0xFF10131F)

private val DarkColorScheme = darkColorScheme(
    primary = OfiBlue,
    onPrimary = Color.White,
    secondary = Gold.copy(alpha = 0.85f),
    onSecondary = Color.White,
    tertiary = Color(0xFF7EA0FF),
    onTertiary = Color.Black,

    background = Color(0xFF0E1020),
    onBackground = Color(0xFFE6E9F2),

    surface = SurfaceDark,
    onSurface = Color(0xFFE6E9F2),

    surfaceVariant = Color(0xFF1A2030),
    onSurfaceVariant = Color(0xFFBCC6D6),

    outline = Color(0xFF2A3144)
)

private val LightColorScheme = lightColorScheme(
    primary = OfiBlue,
    onPrimary = Color.White,
    secondary = Gold,
    onSecondary = Color.White,
    tertiary = Color(0xFF4C7AF2),
    onTertiary = Color.White,

    background = Color.White,
    onBackground = Neutral900,

    surface = SurfaceLight,
    onSurface = Neutral900,

    surfaceVariant = Neutral100,
    onSurfaceVariant = Neutral700,

    outline = Neutral100
)

// ——— Shapes redondeados para look “premium” ———
private val OfiShapes = Shapes(
    extraSmall = RoundedCornerShape(8),
    small = RoundedCornerShape(12),
    medium = RoundedCornerShape(16),
    large = RoundedCornerShape(20),
    extraLarge = RoundedCornerShape(28)
)

@Composable
fun OfiVirtualV3Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Consejo: para preservar marca, puedes poner dynamicColor = false,
    // pero mantenemos true para compatibilidad con tu implementación actual.
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // tu Type.kt existente
        shapes = OfiShapes,
        content = content
    )
}
