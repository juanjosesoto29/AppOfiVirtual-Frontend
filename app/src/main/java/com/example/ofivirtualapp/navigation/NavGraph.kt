package com.example.ofivirtualapp.navigation

import android.app.Application
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
import com.example.ofivirtualapp.repository.AuthRepository
import com.example.ofivirtualapp.ui.theme.components.BottomNavBar
import com.example.ofivirtualapp.ui.theme.screen.*
import com.example.ofivirtualapp.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // --- Creación de ViewModels con sus Factories (ESTO ESTÁ CORRECTO) ---
    val authFactory = AuthViewModelFactory(AuthRepository())
    val authViewModel: AuthViewModel = viewModel(factory = authFactory)

    val application = LocalContext.current.applicationContext as Application
    val settingsFactory = SettingsViewModelFactory(application)
    val settingsViewModel: SettingsViewModel = viewModel(factory = settingsFactory)

    val cartViewModel: CartViewModel = viewModel()
    val perfilViewModel: PerfilViewModel = viewModel()
    // --------------------------------------------------------------------

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
        // 🔹 CORRECCIÓN CLAVE: Todas las rutas están en el nivel superior del NavHost 🔹
        NavHost(
            navController = navController,
            startDestination = Route.Onboarding.path, // La ruta de inicio es Onboarding
            modifier = Modifier.padding(innerPadding)
        ) {
            // --- Nivel 1: Flujo de Autenticación y Onboarding ---
            composable(Route.Onboarding.path) {
                OnboardingScreen(
                    onRegister = {
                        settingsViewModel.completeOnboarding()
                        goTo(Route.Register.path)
                    },
                    onLogin = {
                        settingsViewModel.completeOnboarding()
                        goTo(Route.Login.path)
                    },
                    onSkip = {
                        settingsViewModel.completeOnboarding()
                        // Navega a Home y limpia el historial
                        navigateAndClearStack(Route.Home.path)
                    }
                )
            }

            composable(Route.Register.path) {
                RegisterScreen(
                    authViewModel = authViewModel,
                    onRegistered = {
                        navController.navigate(Route.Login.path) {
                            popUpTo(Route.Register.path) { inclusive = true }
                        }
                    },
                    onGoLogin = { goTo(Route.Login.path) }
                )
            }

            composable(Route.Login.path) {
                LoginScreen(
                    authViewModel = authViewModel,
                    onLoginOk = {
                        settingsViewModel.login(token = "FAKE_JWT_123", remember = true)
                        // Navega a Home y limpia el historial
                        navigateAndClearStack(Route.Home.path)
                    },
                    onGoRegister = { goTo(Route.Register.path) }
                )
            }

            composable(Route.PasswordRecovery.path) { PasswordRecoveryScreen(onBack = { navController.popBackStack() }, onRecovered = { goTo(Route.Login.path) }) }

            // --- Nivel 2: Rutas Principales de la App (Después del Login) ---
            composable(Route.Home.path) {
                HomeScreen(
                    onGoTo = goTo,
                    onLogout = {
                        settingsViewModel.logout()
                        navigateAndClearStack(Route.Login.path)
                    },
                    onRenewPlan = { goTo(Route.Servicios.path) },
                    onGoOficinaVirtual = { goTo(Route.OficinaVirtual.path) },
                    onGoContabilidad = { goTo(Route.Contabilidad.path) },
                    onGoFormalizacion = { goTo(Route.Formalizacion.path) },
                    onOpenContrato1 = {},
                    onOpenContrato2 = {}
                )
            }

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

            composable(Route.Perfil.path) {
                PerfilScreen(
                    uiState = perfilState,
                    onAvatarChange = { uri -> perfilViewModel.onAvatarChange(uri) },
                    onCerrarSesion = {
                        authViewModel.logout()
                        settingsViewModel.logout()
                        navController.navigate(Route.Login.path) {
                            popUpTo(Route.Home.path) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onMetodosPago = { goTo(Route.MetodosPago.path) },
                    onNotificaciones = { goTo(Route.Notificaciones.path) },
                    onAyuda = { goTo(Route.Soporte.path) },
                    onRenovarPlan = { goTo(Route.Servicios.path) }
                )
            }

            // --- Nivel 3: Rutas de detalle o secundarias ---
            composable(Route.PlanFull.path) { PlanFullScreen(onNavigateBack = { navController.popBackStack() }, onAddToCart = { servicio -> cartViewModel.addItem(servicio) }) }
            composable(Route.OficinaVirtual.path) { OficinaVirtualScreen(onAddToCart = { planOV -> val servicio = ServicioUI(categoria = CategoriaServicio.OFICINA_VIRTUAL, nombre = planOV.nombre, descripcion = "Plan de ${planOV.duracionMeses} meses. " + planOV.bullets.joinToString(" "), precioCLP = planOV.precioCLP); cartViewModel.addItem(servicio); Toast.makeText(context, "${servicio.nombre} agregado al carrito", Toast.LENGTH_SHORT).show() }) }
            composable(Route.Contabilidad.path) { ContabilidadScreen(onAddToCart = { servicioConta -> val servicio = ServicioUI(categoria = CategoriaServicio.CONTABILIDAD, nombre = servicioConta.nombre, descripcion = servicioConta.descripcion, precioCLP = servicioConta.precioCLP); cartViewModel.addItem(servicio); Toast.makeText(context, "${servicio.nombre} agregado al carrito", Toast.LENGTH_SHORT).show() }) }
            composable(Route.Formalizacion.path) { FormalizacionScreen(onAddToCart = { servicioFormalizacion -> val servicio = ServicioUI(categoria = CategoriaServicio.FORMALIZACION, nombre = servicioFormalizacion.nombre, descripcion = servicioFormalizacion.descripcion, precioCLP = servicioFormalizacion.precioCLP); cartViewModel.addItem(servicio); Toast.makeText(context, "${servicio.nombre} agregado al carrito", Toast.LENGTH_SHORT).show() }) }

            composable(
                route = Route.Checkout.path,
                arguments = listOf(navArgument("total") { type = NavType.IntType })
            ) { backStackEntry ->
                val total = backStackEntry.arguments?.getInt("total") ?: 0
                CheckoutScreen(
                    totalAPagar = total,
                    onNavigateBack = { navController.popBackStack() },
                    onClearCart = { cartViewModel.clearCart() },
                    onPaymentSuccess = {
                        navController.navigate(Route.Home.path) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                )
            }

            composable(Route.Notificaciones.path) { NotificacionesScreen(onNavigateBack = { navController.popBackStack() }) }
            composable(Route.MetodosPago.path) { CenteredTextScreen("Métodos de Pago") }
            composable(Route.FAQ.path) { FaqScreen(onNavigateBack = { navController.popBackStack() }) }
            composable(Route.Soporte.path) { SoporteScreen(onNavigateBack = { navController.popBackStack() }, onGoToFaq = { goTo(Route.FAQ.path) }) }
            composable(Route.AcercaDe.path) { AcercaDeScreen(onNavigateBack = { navController.popBackStack() }) }
        }
    }
}

// Función de ayuda que ya tenías
@Composable
fun CenteredTextScreen(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text, style = MaterialTheme.typography.headlineMedium)
    }
}