package com.example.ui.application

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
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
import com.example.viewmodel.application.ApplicationUiState
import com.example.viewmodel.application.ApplicationViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AflamiApp(
    viewModel: ApplicationViewModel = koinViewModel()
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