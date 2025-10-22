package com.example.ofivirtualapp.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ofivirtualapp.ui.theme.components.BottomNavBar
import com.example.ofivirtualapp.ui.theme.screen.*
import com.example.ofivirtualapp.viewmodel.AuthViewModel
import com.example.ofivirtualapp.viewmodel.CartViewModel
import com.example.ofivirtualapp.viewmodel.PerfilViewModel
// Ya no necesitamos URLEncoder
// import java.net.URLEncoder
// import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val perfilViewModel: PerfilViewModel = viewModel()

    val cartState by cartViewModel.uiState
    val cartSubtotal by cartViewModel.subtotal
    val perfilState by perfilViewModel.uiState
    val context = LocalContext.current

    val goTo: (String) -> Unit = { route -> navController.navigate(route) }
    val navigateAndClearStack: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val screensWithBottomNav = listOf(Route.Home.path, Route.Servicios.path, Route.Carrito.path, Route.Perfil.path)
            if (currentRoute in screensWithBottomNav) {
                BottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Onboarding.path,
            modifier = Modifier.padding(innerPadding)
        ) {
            // ... (Otras rutas como Onboarding, Login, Home, etc., no cambian)

            composable(Route.Onboarding.path) { OnboardingScreen(onRegister = { goTo(Route.Register.path) }, onLogin = { goTo(Route.Login.path) }, onSkip = { navigateAndClearStack(Route.Home.path) }) }
            composable(Route.Register.path) { RegisterScreen(onRegistered = { navController.navigate(Route.Login.path) { popUpTo(Route.Register.path) { inclusive = true } } }, onGoLogin = { goTo(Route.Login.path) }) }
            composable(Route.Login.path) { LoginScreen(onLoginOk = { navigateAndClearStack(Route.Home.path) }, onGoRegister = { goTo(Route.Register.path) }) }
            composable(Route.PasswordRecovery.path) { PasswordRecoveryScreen(onBack = { navController.popBackStack() }, onRecovered = { goTo(Route.Login.path) }) }
            composable(Route.Home.path) { HomeScreen(onGoTo = goTo, onLogout = { navigateAndClearStack(Route.Login.path) }, onRenewPlan = { goTo(Route.Servicios.path) }, onGoOficinaVirtual = { goTo(Route.OficinaVirtual.path) }, onGoContabilidad = { goTo(Route.Contabilidad.path) }, onGoFormalizacion = { goTo(Route.Formalizacion.path) }, onOpenContrato1 = {}, onOpenContrato2 = {}) }
            composable(Route.Servicios.path) { ServiciosScreen(onAddToCart = { servicio -> cartViewModel.addItem(servicio); Toast.makeText(context, "${servicio.nombre} agregado al carrito", Toast.LENGTH_SHORT).show() }, onGoToPlanFull = { goTo(Route.PlanFull.path) }) }
            composable(Route.Carrito.path) {
                CarritoScreen(
                    items = cartState.items,
                    subtotalCLP = cartSubtotal,
                    onRemoveItem = { itemId -> cartViewModel.removeItem(itemId) },
                    onPay = { total ->
                        navController.navigate(Route.Checkout.path.replace("{total}", total.toString()))
                    }
                )
            }
            composable(Route.Perfil.path) { PerfilScreen(uiState = perfilState, onAvatarChange = { uri -> perfilViewModel.onAvatarChange(uri) }, onCerrarSesion = { authViewModel.logout(); navController.navigate(Route.Login.path) { popUpTo(Route.Home.path) { inclusive = true }; launchSingleTop = true } }, onMetodosPago = { goTo(Route.MetodosPago.path) }, onNotificaciones = { goTo(Route.Notificaciones.path) }, onAyuda = { goTo(Route.Soporte.path) }, onRenovarPlan = { goTo(Route.Servicios.path) }) }
            composable(Route.PlanFull.path) { PlanFullScreen(onNavigateBack = { navController.popBackStack() }, onAddToCart = { servicio -> cartViewModel.addItem(servicio) }) }
            composable(Route.OficinaVirtual.path) { OficinaVirtualScreen(onAddToCart = { planOV -> val servicio = ServicioUI(categoria = CategoriaServicio.OFICINA_VIRTUAL, nombre = planOV.nombre, descripcion = "Plan de ${planOV.duracionMeses} meses. " + planOV.bullets.joinToString(" "), precioCLP = planOV.precioCLP); cartViewModel.addItem(servicio); Toast.makeText(context, "${servicio.nombre} agregado al carrito", Toast.LENGTH_SHORT).show() }) }
            composable(Route.Contabilidad.path) { ContabilidadScreen(onAddToCart = { servicioConta -> val servicio = ServicioUI(categoria = CategoriaServicio.CONTABILIDAD, nombre = servicioConta.nombre, descripcion = servicioConta.descripcion, precioCLP = servicioConta.precioCLP); cartViewModel.addItem(servicio); Toast.makeText(context, "${servicio.nombre} agregado al carrito", Toast.LENGTH_SHORT).show() }) }
            composable(Route.Formalizacion.path) { FormalizacionScreen(onAddToCart = { servicioFormalizacion -> val servicio = ServicioUI(categoria = CategoriaServicio.FORMALIZACION, nombre = servicioFormalizacion.nombre, descripcion = servicioFormalizacion.descripcion, precioCLP = servicioFormalizacion.precioCLP); cartViewModel.addItem(servicio); Toast.makeText(context, "${servicio.nombre} agregado al carrito", Toast.LENGTH_SHORT).show() }) }

            // --- RUTA DE CHECKOUT RESTAURADA ---
            composable(
                route = Route.Checkout.path,
                arguments = listOf(navArgument("total") { type = NavType.IntType })
            ) { backStackEntry ->
                val total = backStackEntry.arguments?.getInt("total") ?: 0
                CheckoutScreen(
                    totalAPagar = total,
                    onNavigateBack = { navController.popBackStack() },
                    onClearCart = { cartViewModel.clearCart() }, // Limpia el carrito
                    onPaymentSuccess = { // Navega a Home y limpia el backstack
                        navController.navigate(Route.Home.path) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                )
            }

            // --- RUTAS RESTANTES (YA NO INCLUYEN EL WEBVIEW) ---
            composable(Route.Notificaciones.path) {
                NotificacionesScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Route.MetodosPago.path) { CenteredTextScreen("Métodos de Pago") }
            composable(Route.FAQ.path) {
                FaqScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Route.Soporte.path) {
                SoporteScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onGoToFaq = { goTo(Route.FAQ.path) }
                )
            }
            composable(Route.AcercaDe.path) {
                AcercaDeScreen(onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
fun CenteredTextScreen(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text, style = MaterialTheme.typography.headlineMedium)
    }
}