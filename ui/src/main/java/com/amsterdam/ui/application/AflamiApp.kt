package com.amsterdam.ui.application

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.amsterdam.designsystem.components.Scaffold
import com.amsterdam.designsystem.components.snackBar.SnackBarHost
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.ui.components.bottomNavigation.BottomNavigation
import com.amsterdam.ui.navigation.NavGraph
import com.amsterdam.viewmodel.application.ApplicationViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun AflamiApp(
    viewModel: ApplicationViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val startDestination = runBlocking {
        getStartDestination(viewModel.setStartDestination())
    }
    AflamiTheme {
        CompositionLocalProvider(LocalNavController provides navController) {
            Scaffold(
                bottomBar = {
                    BottomNavigation(
                        currentDestination = currentDestination,
                        onNavigate = {
                            navController.navigate(it) {
                                popUpTo(it) {
                                    inclusive = true
                                }
                            }
                        },
                    )
                }
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    NavGraph(
                        navController = navController,
                        startDestination = startDestination
                    )
                    SnackBarHost()
                }
            }
        }
    }
}

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("NavController not found")
}