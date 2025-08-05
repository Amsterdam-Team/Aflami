package com.amsterdam.ui.application

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.amsterdam.designsystem.components.Scaffold
import com.amsterdam.designsystem.components.snackBar.SnackBarHost
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.domain.utils.RestrictionLevel
import com.amsterdam.ui.components.bottomNavigation.BottomNavigation
import com.amsterdam.ui.navigation.NavGraph
import com.amsterdam.ui.navigation.Route
import com.amsterdam.ui.utils.setLocale
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
    val state by viewModel.state.collectAsState()
    val localConfigurations by rememberUpdatedState(LocalConfiguration.current)
    LaunchedEffect(Unit) {
        viewModel.initAppSettings(localConfigurations.locales[0])
    }
    val restrictionLevel = state.restrictionLevel

    val layoutDirection =
        if (state.language.value == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr


    val context = LocalContext.current
    context.setLocale(state.language.value)

    AflamiTheme(
        isDarkTheme = state.isDarkTheme,
    ) {
        CompositionLocalProvider(
            LocalNavController provides navController,
            LocalRestrictionLevel provides restrictionLevel
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                Scaffold(
                    bottomBar = {
                        BottomNavigation(
                            currentDestination = currentDestination,
                            onNavigate = {
                                navController.navigate(it) {
                                    popUpTo(Route.Tab.Home) {
                                        saveState = true
                                    }

                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    }
                ) { paddingValues ->
                    CompositionLocalProvider(
                        LocalScaffoldBottomPadding provides paddingValues.calculateBottomPadding()
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
    }
}

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("NavController not found")
}

val LocalRestrictionLevel = compositionLocalOf<RestrictionLevel> {
    error("No Restriction Level")
}

val LocalScaffoldBottomPadding = compositionLocalOf { 0.dp }
