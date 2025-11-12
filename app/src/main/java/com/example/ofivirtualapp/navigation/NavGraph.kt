package com.example.ofivirtualapp.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ofivirtualapp.data.local.storage.UserPreferences
import com.example.ofivirtualapp.ui.theme.components.BottomNavBar
import com.example.ofivirtualapp.ui.theme.screen.*
import com.example.ofivirtualapp.viewmodel.AuthViewModel
import com.example.ofivirtualapp.viewmodel.CartViewModel
import com.example.ofivirtualapp.viewmodel.PerfilViewModel
import com.example.ofivirtualapp.viewmodel.EmpresaViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalUriHandler

@Composable
fun AppNavGraph(
    authViewModel: AuthViewModel,
    perfilViewModel: PerfilViewModel,
    empresaViewModel: EmpresaViewModel
) {

    val navController = rememberNavController()
    val cartViewModel: CartViewModel = viewModel()
    val cartCount by cartViewModel.cartCount

    val goTo: (String) -> Unit = { route -> navController.navigate(route) }
    val goHome: () -> Unit = { navController.navigate(Route.Home.path) }
    val goLogin: () -> Unit = { navController.navigate(Route.Login.path) }
    val goRegister: () -> Unit = { navController.navigate(Route.Register.path) }
    val navigateAndClearStack: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }

    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val userPrefs = remember { UserPreferences(context) }
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val screensWithBottomNav = listOf(Route.Home.path, Route.Servicios.path, Route.Carrito.path, Route.Perfil.path)
            if (currentRoute in screensWithBottomNav) {
                BottomNavBar(
                    navController = navController,
                    cartCount = cartCount,  // 🔹 se actualiza automáticamente
                    profileDot = false    // 👈 si quieres mostrar el puntito rojo en el perfil
                )

            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Splash.path,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Route.Splash.path) {
                SplashScreen(
                    onFinished = {
                        navController.navigate(Route.Onboarding.path) {
                            popUpTo(Route.Splash.path) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Route.Onboarding.path) {
                OnboardingScreen(
                    onLogin = goLogin,
                    onRegister = goRegister,
                    onSkip = { navigateAndClearStack(Route.Home.path) }
                )
            }
            composable(Route.Home.path) {
                HomeScreen(
                    onGoTo = goTo,
                    onLogout = {
                        scope.launch {
                            userPrefs.clearSession()
                            navigateAndClearStack(Route.Onboarding.path)
                        }
                    },
                    onRenewPlan = { goTo(Route.OficinaVirtual.path) },
                    onGoOficinaVirtual = { goTo(Route.OficinaVirtual.path) },
                    onGoContabilidad = { goTo(Route.Contabilidad.path) },
                    onGoFormalizacion = { goTo(Route.Formalizacion.path) },
                    onOpenContrato1 = { url ->
                        try {
                            uriHandler.openUri(url)
                        } catch (e: Exception) {
                            Toast.makeText(context, "No se ha cargado el contrato", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onOpenContrato2 = { url ->
                        try {
                            uriHandler.openUri(url)
                        } catch (e: Exception) {
                            Toast.makeText(context, "No se ha cargado el contrato", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
            composable(Route.Login.path) {
                LoginScreenVm(
                    vm = authViewModel,
                    userPrefs = userPrefs,
                    onLoginOkNavigateHome = { navigateAndClearStack(Route.Home.path) },
                    onGoRegister = goRegister
                )
            }
            composable(Route.Register.path) {
                RegisterScreenVm(
                    vm = authViewModel,
                    onRegisteredNavigateLogin = goLogin,
                    onGoLogin = goLogin
                )
            }
            composable(Route.Perfil.path) {
                val perfilState by perfilViewModel.uiState.collectAsState()
                PerfilScreen(
                    perfilViewModel = perfilViewModel,
                    uiState = perfilState,
                    onAvatarChange = { perfilViewModel.onAvatarChange(it) },
                    onVerContratos = { goTo(Route.MisContratos.path) },
                    onMetodosPago = { goTo(Route.MetodosPago.path) },
                    onNotificaciones = { goTo(Route.Notificaciones.path) },
                    onAyuda = { goTo(Route.Soporte.path) },
                    onRenovarPlan = { goTo(Route.OficinaVirtual.path) },
                    onDatosEmpresa = { goTo(Route.Empresa.path) },

                    onCerrarSesion = {
                        scope.launch {
                            userPrefs.clearSession()
                            navigateAndClearStack(Route.Onboarding.path)
                        }
                    }
                )
            }
            composable(Route.MisContratos.path) {
                MisContratosScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onOpenContrato = { url ->
                        try {
                            uriHandler.openUri(url)
                        } catch (e: Exception) {
                            Toast.makeText(context, "No se pudo abrir el contrato", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
            composable(Route.Empresa.path) {
                EmpresaScreenVm(
                    vm = empresaViewModel,
                    onGoBack = { navController.popBackStack() }
                )
            }
            composable(Route.Servicios.path) {
                ServiciosScreen(
                    onAddToCart = { servicio ->
                        Toast.makeText(context, "${servicio.nombre} agregado", Toast.LENGTH_SHORT).show()
                        cartViewModel.addItem(servicio)
                    },
                    onGoToPlanFull = { goTo(Route.PlanFull.path) }
                )
            }
            composable(Route.Carrito.path) {
                val cartState by cartViewModel.uiState
                val subtotal by cartViewModel.subtotal
                CarritoScreen(
                    items = cartState.items,
                    subtotalCLP = subtotal,
                    onRemoveItem = { cartViewModel.removeItem(it) },
                    onPay = { total ->
                        navController.navigate(Route.Checkout.path.replace("{total}", total.toString()))
                    }
                )
            }

            composable(Route.OficinaVirtual.path) {
                OficinaVirtualScreenVm(
                    onAddToCart = { planOV ->
                        val servicio = ServicioUI(
                            categoria = CategoriaServicio.OFICINA_VIRTUAL,
                            nombre = planOV.nombre,
                            descripcion = "Plan de ${planOV.duracionMeses} meses. " + planOV.bullets.joinToString(" "),
                            precioCLP = planOV.precioCLP
                        )
                        cartViewModel.addItem(servicio)
                        Toast.makeText(context, "${servicio.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Route.Contabilidad.path) {
                ContabilidadScreen(onAddToCart = { servicioConta ->
                    val servicio = ServicioUI(
                        categoria = CategoriaServicio.CONTABILIDAD,
                        nombre = servicioConta.nombre,
                        descripcion = servicioConta.descripcion,
                        precioCLP = servicioConta.precioCLP
                    )
                    cartViewModel.addItem(servicio)
                    Toast.makeText(context, "${servicio.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
                })
            }
            composable(Route.Formalizacion.path) {
                FormalizacionScreen(onAddToCart = { servicioFormalizacion ->
                    val servicio = ServicioUI(
                        categoria = CategoriaServicio.FORMALIZACION,
                        nombre = servicioFormalizacion.nombre,
                        descripcion = servicioFormalizacion.descripcion,
                        precioCLP = servicioFormalizacion.precioCLP
                    )
                    cartViewModel.addItem(servicio)
                    Toast.makeText(context, "${servicio.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
                })
            }
            composable(Route.PlanFull.path) {
                PlanFullScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onAddToCart = { servicio -> cartViewModel.addItem(servicio) }
                )
            }

            composable(Route.Notificaciones.path) { NotificacionesScreen(onNavigateBack = { navController.popBackStack() }) }
            composable(Route.MetodosPago.path) {
                MetodosPagoScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onOpenWhatsApp = { url ->
                        try {
                            uriHandler.openUri(url)
                        } catch (e: Exception) {
                            Toast.makeText(context, "No se pudo abrir WhatsApp", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
            composable(Route.FAQ.path) { FaqScreen(onNavigateBack = { navController.popBackStack() }) }
            composable(Route.Soporte.path) {
                SoporteScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onGoToFaq = { goTo(Route.FAQ.path) }
                )
            }
            composable(Route.AcercaDe.path) { AcercaDeScreen(onNavigateBack = { navController.popBackStack() }) }
            composable(
                route = Route.Checkout.path,
                arguments = listOf(navArgument("total") { type = NavType.IntType })
            ) { backStackEntry ->
                val total = backStackEntry.arguments?.getInt("total") ?: 0
                CheckoutScreen(
                    totalAPagar = total,
                    onNavigateBack = { navController.popBackStack() },
                    onClearCart = { cartViewModel.clearCart() },
                    onPaymentSuccess = { navigateAndClearStack(Route.Home.path) }
                )
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(text: String, navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Volver")
            }
        }
    }
}
