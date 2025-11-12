package com.example.ofivirtualapp.ui.theme

import androidx.compose.ui.graphics.Color

// === Colores base (mantiene tus valores originales) ===
val OfiBlue = Color(0xFF071290) // Azul corporativo
val Gold = Color(0xFFDAA520)     // Dorado de acento
val White = Color(0xFFFFFFFF)
val Ink = Color(0xFF111111)      // Texto oscuro

// === Tonos complementarios ===
// (útiles para fondos, degradados, sombras o separadores)
val OfiBlueLight = Color(0xFF1D2EEB)   // Azul más claro (hover o gradiente)
val OfiBlueDark = Color(0xFF040C63)    // Azul más oscuro (fondo oscuro)
val GoldLight = Color(0xFFFFD85A)      // Dorado claro (hover o gradiente)
val GoldDark = Color(0xFFB8860B)       // Dorado oscuro (texto/acento)
val NeutralGray = Color(0xFFEEEEEE)    // Fondo suave
val NeutralBorder = Color(0xFFD5D9E0)  // Bordes o separadores

// === Sistema de retrocompatibilidad (Material3) ===
// Si alguna pantalla vieja usa los colores default de Material 3
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
