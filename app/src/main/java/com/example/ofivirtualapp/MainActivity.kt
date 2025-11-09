package com.example.ofivirtualapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.ofivirtualapp.navigation.AppNavGraph
import com.example.ofivirtualapp.ui.theme.OfiVirtualV3Theme
import com.example.ofivirtualapp.data.local.storage.UserPreferences
import com.example.ofivirtualapp.viewmodel.AuthViewModel
import com.example.ofivirtualapp.viewmodel.PerfilViewModel
import com.example.ofivirtualapp.viewmodel.PerfilViewModelFactory
import com.example.ofivirtualapp.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppRoot()
        }
    }
}

@Composable
fun AppRoot() {
    val context = LocalContext.current.applicationContext

    // ViewModel de autenticación (sin factory)
    val authViewModel: AuthViewModel = viewModel()

    // Preferencias de usuario
    val userPreferences = remember { UserPreferences(context) }

    // ViewModel de perfil: ahora solo necesita UserPreferences
    val perfilViewModel: PerfilViewModel = viewModel(
        factory = PerfilViewModelFactory(userPreferences)
    )

    val empresaViewModel: EmpresaViewModel = viewModel(
        factory = EmpresaViewModelFactory(userPreferences)
    )

    val navController = rememberNavController()

    OfiVirtualV3Theme {
        AppNavGraph(
            authViewModel = authViewModel,
            perfilViewModel = perfilViewModel,
            empresaViewModel = empresaViewModel
        )
    }
}
