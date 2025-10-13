package com.example.ofivirtualapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.example.ofivirtualapp.navigation.AppNavigation // 1. CAMBIO: Importa la función correcta
import com.example.ofivirtualapp.ui.theme.OfiVirtualV3Theme // Asegúrate de importar tu tema

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita el modo de pantalla completa (edge-to-edge)
        setContent {
            // Llama a la función raíz de tu UI
            AppRoot()
        }
    }
}

@Composable
fun AppRoot() {
    // 2. CAMBIO: Usa el tema de tu aplicación que definiste
    OfiVirtualV3Theme {
        // 3. CAMBIO: Llama a tu función de navegación directamente.
        // Ya no necesitas crear ni pasar el navController aquí.
        AppNavigation()
    }
}
