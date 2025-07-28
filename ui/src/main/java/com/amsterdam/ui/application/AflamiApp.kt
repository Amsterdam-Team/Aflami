package com.amsterdam.ui.application

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.amsterdam.designsystem.components.Scaffold
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.ui.navigation.BottomNavigation
import com.amsterdam.ui.navigation.NavGraph
import com.amsterdam.ui.utils.safeNavigateToTab
import com.amsterdam.viewmodel.application.ApplicationViewModel

@Composable
fun AflamiApp(
    viewModel: ApplicationViewModel = hiltViewModel()
){
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val state = viewModel.state.collectAsState()
    val startDestination = getStartDestination(state.value.startDestination)
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
                    startDestination = startDestination
                )
            }
        }
    }
}

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("NavController not found")
}