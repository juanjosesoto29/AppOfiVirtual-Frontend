package com.example.ofivirtualapp.ui.theme.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ofivirtualapp.navigation.Destination

@Composable
fun BottomNavBar(
    navController: NavController,
    cartCount: Int = 0,         // 🔹 badge numérico para Carrito
    profileDot: Boolean = false // 🔹 punto (dot) para Perfil
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        Destination.HOME,
        Destination.SERVICIOS,
        Destination.CARRITO,
        Destination.PERFIL
    )

    if (currentRoute in items.map { it.route }) {
        NavigationBar(
            tonalElevation = 3.dp,
            modifier = Modifier.windowInsetsPadding(
                WindowInsets.safeDrawing.only(androidx.compose.foundation.layout.WindowInsetsSides.Bottom)
            )
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route

                // Animaciones sutiles
                val iconSize by animateDpAsState(
                    targetValue = if (selected) 28.dp else 22.dp,
                    animationSpec = tween(180), label = "iconSize"
                )
                val labelAlpha by animateFloatAsState(
                    targetValue = if (selected) 1f else 0.65f,
                    animationSpec = tween(180), label = "labelAlpha"
                )

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { start ->
                                popUpTo(start) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        // ----- BADGES -----
                        val isCart = item.route == Destination.CARRITO.route
                        val isProfile = item.route == Destination.PERFIL.route

                        val showNumberBadge = isCart && cartCount > 0
                        val showDotBadge = isProfile && profileDot

                        if (showNumberBadge || showDotBadge) {
                            BadgedBox(
                                badge = {
                                    if (showNumberBadge) {
                                        Badge {
                                            Text(
                                                text = cartCount.coerceAtMost(99).toString()
                                            )
                                        }
                                    } else if (showDotBadge) {
                                        // Punto sin número (Badge vacío)
                                        Badge()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.contentDescription,
                                    modifier = Modifier.size(iconSize)
                                )
                            }
                        } else {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.contentDescription,
                                modifier = Modifier.size(iconSize)
                            )
                        }
                    },
                    label = {
                        AnimatedVisibility(visible = true) {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .alpha(labelAlpha)
                            )
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}
