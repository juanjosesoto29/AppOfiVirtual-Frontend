package com.example.ofivirtualapp.navigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ofivirtualapp.ui.theme.components.AppDrawer
import com.example.ofivirtualapp.ui.theme.components.BottomNavBar
import com.example.ofivirtualapp.ui.theme.components.defaultDrawerItems
import com.example.ofivirtualapp.ui.theme.screen.*
import com.example.ofivirtualapp.viewmodel.AuthViewModel
import com.example.ofivirtualapp.viewmodel.PerfilViewModel
import com.example.ofivirtualapp.viewmodel.CartViewModel
import kotlinx.coroutines.launch

@Composable // Gráfico de navegación + Drawer + Scaffold
fun AppNavGraph(navController: NavHostController,
                authViewModel: AuthViewModel,
                cartViewModel: CartViewModel,
                perfilViewModel: PerfilViewModel // <-- 1.- NUEVO: recibimos el VM inyectado desde MainActivity
) { // Recibe el controlador

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed) // Estado del drawer
    val scope = rememberCoroutineScope() // Necesario para abrir/cerrar drawer

    // Helpers de navegación (reutilizamos en topbar/drawer/botones)
    val goHome: () -> Unit    = { navController.navigate(Route.Home.path) }    // Ir a Home
    val goLogin: () -> Unit   = { navController.navigate(Route.Login.path) }   // Ir a Login
    val goRegister: () -> Unit = { navController.navigate(Route.Register.path) } // Ir a Registro
    val goTo: (String) -> Unit = { route -> navController.navigate(route) }
    ModalNavigationDrawer( // Capa superior con drawer lateral
        drawerState = drawerState, // Estado del drawer
        drawerContent = { // Contenido del drawer (menú)
            AppDrawer( // Nuestro componente Drawer
                currentRoute = null, // Puedes pasar navController.currentBackStackEntry?.destination?.route
                items = defaultDrawerItems( // Lista estándar
                    onHome = {
                        scope.launch { drawerState.close() } // Cierra drawer
                        goHome() // Navega a Home
                    },
                    onLogin = {
                        scope.launch { drawerState.close() } // Cierra drawer
                        goLogin() // Navega a Login
                    },
                    onRegister = {
                        scope.launch { drawerState.close() } // Cierra drawer
                        goRegister() // Navega a Registro
                    }
                )
            )
        }
    ) {
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
                startDestination = Route.Home.path, // Inicio: Home
                modifier = Modifier.padding(innerPadding) // Respeta topBar
            ) {
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
                            authViewModel.logout()
                            navigateAndClearStack(Route.Onboarding.path)
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
                composable(Route.OficinaVirtual.path) { PlaceholderScreen("Oficina Virtual", navController) }
                composable(Route.Contabilidad.path) { PlaceholderScreen("Contabilidad", navController) }
                composable(Route.Formalizacion.path) { PlaceholderScreen("Formalización", navController) }
                composable(Route.Notificaciones.path) { PlaceholderScreen("Notificaciones", navController) }
                composable(Route.MetodosPago.path) { PlaceholderScreen("Métodos de Pago", navController) }
                composable(Route.FAQ.path) { PlaceholderScreen("Preguntas Frecuentes", navController) }
                composable(Route.Soporte.path) { PlaceholderScreen("Soporte", navController) }
                composable(Route.AcercaDe.path) { PlaceholderScreen("Acerca De", navController) }
                composable(Route.PlanFull.path) { PlaceholderScreen("Plan Full", navController) }

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

}



