package com.example.ofivirtualapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Route(val path: String) {
    object Splash : Route("splash")
    object Onboarding : Route("onboarding")

    object Login : Route("login")
    object Register : Route("register")

    object Home : Route("home")
    object Perfil : Route("perfil")
    object Carrito : Route("carrito")
    object Servicios : Route("servicios")
    object OficinaVirtual : Route("oficina_virtual")
    object Contabilidad : Route("contabilidad")
    object Formalizacion : Route("formalizacion")
    object Notificaciones : Route("notificaciones")
    object MetodosPago : Route("metodos_pago")
    object FAQ : Route("faq")
    object Soporte : Route("soporte")
    object AcercaDe : Route("acerca_de")
    object PlanFull : Route("plan_full")
    object Checkout : Route("checkout/{total}")
    object MisContratos : Route("mis_contratos")
    object Empresa : Route("empresa")



}

enum class Destination(
    val route: String,
    val icon: ImageVector,
    val label: String,
    val contentDescription: String
) {
    HOME(Route.Home.path, Icons.Default.Home, "Inicio", "Inicio"),
    SERVICIOS(Route.Servicios.path, Icons.AutoMirrored.Filled.List, "Servicios", "Servicios"), // ¡USO CORREGIDO!
    CARRITO(Route.Carrito.path, Icons.Default.ShoppingCart, "Carrito", "Carrito"),
    PERFIL(Route.Perfil.path, Icons.Default.Person, "Perfil", "Perfil")
}
