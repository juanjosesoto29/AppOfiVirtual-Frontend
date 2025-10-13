package com.example.ofivirtualapp.ui.theme.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ofivirtualapp.navigation.Destination

@Composable
fun BottomNavBar(navController: NavController) {
    // Obtenemos la ruta actual para saber qué ítem resaltar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Lista de destinos que se mostrarán en la barra de navegación
    val navigationItems = listOf(
        Destination.HOME,
        Destination.SERVICIOS,
        Destination.CARRITO,
        Destination.PERFIL
    )

    // Solo mostramos la barra de navegación si la ruta actual es una de las principales
    if (currentRoute in navigationItems.map { it.route }) {
        NavigationBar {
            navigationItems.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.route, // Resalta el ítem si la ruta coincide
                    onClick = {
                        navController.navigate(item.route) {
                            // Navega al inicio del grafo para evitar acumular pantallas
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            // Evita crear una nueva instancia de la misma pantalla
                            launchSingleTop = true
                            // Restaura el estado al volver a seleccionar un ítem
                            restoreState = true
                        }
                    },
                    icon = { Icon(item.icon, contentDescription = item.contentDescription) },
                    label = { Text(item.label) }
                )
            }
        }
    }
}
