package com.example.ofivirtualapp.navigation

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ofivirtualapp.ui.theme.components.BottomNavBar
import com.example.ofivirtualapp.ui.theme.screen.*
import com.example.ofivirtualapp.viewmodel.AuthViewModel
import com.example.ofivirtualapp.viewmodel.PerfilViewModel
import com.example.ofivirtualapp.viewmodel.CartViewModel
import com.example.ofivirtualapp.data.local.storage.UserPreferences
import kotlinx.coroutines.launch

@Composable // Gráfico de navegación + Drawer + Scaffold
fun AppNavGraph(authViewModel: AuthViewModel) {

    // 🔹 CORRECCIÓN 2: Creamos todos los ViewModels y el NavController aquí.
    val navController = rememberNavController()
    val cartViewModel: CartViewModel = viewModel()
    val perfilViewModel: PerfilViewModel = viewModel()


    // 🔹 CORRECCIÓN 3: Definimos TODAS las funciones de navegación que usarás.
    val goTo: (String) -> Unit = { route -> navController.navigate(route) }
    val goHome: () -> Unit    = { navController.navigate(Route.Home.path) }
    val goLogin: () -> Unit   = { navController.navigate(Route.Login.path) }
    val goRegister: () -> Unit = { navController.navigate(Route.Register.path) }
    // Esta función faltaba y causaba error en HomeScreen
    val navigateAndClearStack: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }

    val context = LocalContext.current

    val userPrefs = remember { UserPreferences(context) }
    val scope = rememberCoroutineScope()



    // 🔹 CORRECCIÓN 4: Eliminamos ModalNavigationDrawer y usamos solo Scaffold.
    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val screensWithBottomNav = listOf(Route.Home.path, Route.Servicios.path, Route.Carrito.path, Route.Perfil.path)
            if (currentRoute in screensWithBottomNav) {
                BottomNavBar(navController = navController)
            }
        }
    ) { innerPadding -> // Padding que evita solapar contenido
            NavHost( // Contenedor de destinos navegables
                navController = navController, // Controlador
                startDestination = Route.Onboarding.path, // Inicio: Home
                modifier = Modifier.padding(innerPadding) // Respeta topBar
            ) {
                composable(Route.Onboarding.path) { OnboardingScreen(onRegister = { goTo(Route.Register.path) }, onLogin = { goTo(Route.Login.path) }, onSkip = { navigateAndClearStack(Route.Home.path) }) }
                composable(Route.Home.path) { HomeScreen(onGoTo = goTo, onLogout = { navigateAndClearStack(Route.Login.path) }, onRenewPlan = { goTo(Route.Servicios.path) }, onGoOficinaVirtual = { goTo(Route.OficinaVirtual.path) }, onGoContabilidad = { goTo(Route.Contabilidad.path) }, onGoFormalizacion = { goTo(Route.Formalizacion.path) }, onOpenContrato1 = {}, onOpenContrato2 = {}) }
                composable(Route.Login.path) { // Destino Login
                    //1 modificamos el acceso a la pagina
                    // Usamos la versión con ViewModel (LoginScreenVm) para formularios/validación en tiempo real
                    LoginScreenVm(
                        vm = authViewModel,            // <-- NUEVO: pasamos VM inyectado
                        onLoginOkNavigateHome = goHome,            // Si el VM marca success=true, navegamos a Home
                        onGoRegister = goRegister                  // Enlace para ir a la pantalla de Registro
                    )
                }
                composable(Route.Register.path) { // Destino Registro
                    //2 modificamos el acceso a la pagina
                    // Usamos la versión con ViewModel (RegisterScreenVm) para formularios/validación en tiempo real
                    RegisterScreenVm(
                        vm = authViewModel,            // <-- NUEVO: pasamos VM inyectado
                        onRegisteredNavigateLogin = goLogin,       // Si el VM marca success=true, volvemos a Login
                        onGoLogin = goLogin                        // Botón alternativo para ir a Login
                    )
                }
                composable(Route.Perfil.path) {
                    val perfilState by perfilViewModel.uiState // State se accede con .value
                    PerfilScreen(
                        uiState = perfilState, // Pasamos el estado
                        onAvatarChange = { perfilViewModel.onAvatarChange(it) },
                        onMetodosPago = { goTo(Route.MetodosPago.path) },
                        onNotificaciones = { goTo(Route.Notificaciones.path) },
                        onAyuda = { goTo(Route.Soporte.path) },
                        onRenovarPlan = { goTo(Route.Servicios.path) },
                        onCerrarSesion = {
                            scope.launch {
                                userPrefs.setLoggedIn(false) // Borra la preferencia
                                navigateAndClearStack(Route.Onboarding.path) // Navega al Onboarding
                            }
                        }
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

                // --- Resto de Rutas (usamos un placeholder temporal) ---
                composable(Route.PasswordRecovery.path) { PlaceholderScreen("Recuperar Contraseña", navController) }
                composable(Route.OficinaVirtual.path) { OficinaVirtualScreen(onAddToCart = { planOV -> val servicio = ServicioUI(categoria = CategoriaServicio.OFICINA_VIRTUAL, nombre = planOV.nombre, descripcion = "Plan de ${planOV.duracionMeses} meses. " + planOV.bullets.joinToString(" "), precioCLP = planOV.precioCLP); cartViewModel.addItem(servicio); Toast.makeText(context, "${servicio.nombre} agregado al carrito", Toast.LENGTH_SHORT).show() }) }
                composable(Route.Contabilidad.path) { ContabilidadScreen(onAddToCart = { servicioConta -> val servicio = ServicioUI(categoria = CategoriaServicio.CONTABILIDAD, nombre = servicioConta.nombre, descripcion = servicioConta.descripcion, precioCLP = servicioConta.precioCLP); cartViewModel.addItem(servicio); Toast.makeText(context, "${servicio.nombre} agregado al carrito", Toast.LENGTH_SHORT).show() }) }
                composable(Route.Contabilidad.path) { ContabilidadScreen(onAddToCart = { servicioConta -> val servicio = ServicioUI(categoria = CategoriaServicio.CONTABILIDAD, nombre = servicioConta.nombre, descripcion = servicioConta.descripcion, precioCLP = servicioConta.precioCLP); cartViewModel.addItem(servicio); Toast.makeText(context, "${servicio.nombre} agregado al carrito", Toast.LENGTH_SHORT).show() }) }
                composable(Route.Notificaciones.path) {
                    NotificacionesScreen(onNavigateBack = { navController.popBackStack() })
                }
                composable(Route.MetodosPago.path) { PlaceholderScreen("EN PROCESO", navController) }
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
                composable(Route.PlanFull.path) { PlanFullScreen(onNavigateBack = { navController.popBackStack() }, onAddToCart = { servicio -> cartViewModel.addItem(servicio) }) }

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

    // Composable de ayuda para las pantallas no implementadas
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





