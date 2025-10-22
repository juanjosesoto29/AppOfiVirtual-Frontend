package com.example.ofivirtualapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
// import androidx.activity.enableEdgeToEdge // 1. COMENTAMOS ESTA LÍNEA. Es la causa más probable del crash.

import androidx.compose.runtime.Composable
import com.example.ofivirtualapp.navigation.AppNavigation
import com.example.ofivirtualapp.ui.theme.OfiVirtualV3Theme // Usamos el tema que ya tienes y funciona.

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // enableEdgeToEdge() // DESACTIVADO TEMPORALMENTE

        setContent {
            // Llamamos directamente al Composable raíz que ya tienes.
            // Esto respeta tu estructura original.
            AppRoot()
        }
    }
}

@Composable
fun AppRoot() {
    // Usamos el tema correcto que tú definiste.
    OfiVirtualV3Theme {
        // Tu NavGraph ya tiene un Scaffold interno, por lo que no necesita
        // un Surface adicional aquí que pueda causar conflictos de dibujado.
        // Simplemente llamamos a la navegación.
        AppNavigation()
    }
}
