package com.example.ui.application

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.designsystem.components.Scaffold
import com.example.designsystem.theme.AflamiTheme
import com.example.ui.navigation.BottomNavigation
import com.example.ui.navigation.NavGraph
import com.example.ui.navigation.Route
import com.example.ui.utils.safeNavigate
import com.example.ui.utils.safeNavigateToTab

@Composable
fun AflamiApp(){
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    AflamiTheme {
        CompositionLocalProvider(LocalNavController provides navController) {
            Scaffold(
                bottomBar = {
                    BottomNavigation(
                        currentDestination = currentDestination,
                        onNavigate = { navController.safeNavigateToTab(it) },
                    )
                }
            ) {
                NavGraph(
                    navController = navController,
                    startDestination = Route.Tab.Home
                )
            }
        }
    }
}

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("NavController not found")
}