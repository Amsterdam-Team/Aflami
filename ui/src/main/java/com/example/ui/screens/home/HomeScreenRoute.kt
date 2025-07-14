package com.example.ui.screens.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ui.navigation.Route

fun NavGraphBuilder.homeScreenRoute() {
    composable<Route.Tab.Home> {
        HomeScreen()
    }
}