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
import com.example.ofivirtualapp.data.local.database.AppDatabase
import com.example.ofivirtualapp.data.repository.UserRepository
import com.example.ofivirtualapp.viewmodel.AuthViewModel
import com.example.ofivirtualapp.viewmodel.AuthViewModelFactory
import com.example.ofivirtualapp.data.local.storage.UserPreferences
import com.example.ofivirtualapp.viewmodel.PerfilViewModel
import com.example.ofivirtualapp.viewmodel.PerfilViewModelFactory

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

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            UserRepository(
                AppDatabase.getInstance(context).userDao()
            )
        )
    )
    val userPreferences = remember { UserPreferences(context) }
    // ^ Obtenemos el applicationContext para construir la base de datos de Room.

    val db = AppDatabase.getInstance(context)
    // ^ Singleton de Room. No crea múltiples instancias.

    val userDao = db.userDao()
    // ^ Obtenemos el DAO de usuarios desde la DB.

    val userRepository = UserRepository(userDao)
    // ^ Repositorio que encapsula la lógica de login/registro contra Room.


    // ^ Creamos el ViewModel con factory para inyectar el repositorio.
    val perfilViewModel: PerfilViewModel = viewModel(
        factory = PerfilViewModelFactory(userRepository, userPreferences)
    )
    //   Esto reemplaza cualquier uso anterior de listas en memoria (USERS).


    val navController = rememberNavController() // Controlador de navegación (igual que antes)
    OfiVirtualV3Theme {

        AppNavGraph(
            authViewModel = authViewModel,
            perfilViewModel = perfilViewModel // Añadimos el nuevo parámetro
        )
    }
}
