package com.amsterdam.ui.screens.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.homeScreenRoute(){
    composable<Route.Tab.Home> {
        HomeScreen()
    }
}