package com.example.ofivirtualapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
// import androidx.activity.enableEdgeToEdge // 1. COMENTAMOS ESTA LÍNEA. Es la causa más probable del crash.

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.ofivirtualapp.navigation.AppNavGraph
import com.example.ofivirtualapp.ui.theme.OfiVirtualV3Theme
import com.example.uinavegacion.data.local.database.AppDatabase
import com.example.uinavegacion.data.repository.UserRepository
import com.example.uinavegacion.ui.viewmodel.AuthViewModel
import com.example.uinavegacion.ui.viewmodel.AuthViewModelFactory// Usamos el tema que ya tienes y funciona.

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
    val context = LocalContext.current.applicationContext

    // ^ Obtenemos el applicationContext para construir la base de datos de Room.

    val db = AppDatabase.getInstance(context)
    // ^ Singleton de Room. No crea múltiples instancias.

    val userDao = db.userDao()
    // ^ Obtenemos el DAO de usuarios desde la DB.

    val userRepository = UserRepository(userDao)
    // ^ Repositorio que encapsula la lógica de login/registro contra Room.

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(userRepository)
    )
    // ^ Creamos el ViewModel con factory para inyectar el repositorio.
    //   Esto reemplaza cualquier uso anterior de listas en memoria (USERS).

    // ====== TU NAVEGACIÓN ORIGINAL ======
    val navController = rememberNavController() // Controlador de navegación (igual que antes)
    OfiVirtualV3Theme {
        // Tu NavGraph ya tiene un Scaffold interno, por lo que no necesita
        // un Surface adicional aquí que pueda causar conflictos de dibujado.
        // Simplemente llamamos a la navegación.
        AppNavGraph(
            navController = navController,
            authViewModel = authViewModel // <-- NUEVO parámetro
        )
    }
}
