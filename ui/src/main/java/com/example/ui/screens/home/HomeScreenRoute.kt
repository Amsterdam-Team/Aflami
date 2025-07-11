package com.example.ui.screens.home

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ui.navigation.Route
import com.example.ui.screens.profile.ProfileScreen

fun NavGraphBuilder.homeScreenRoute(){
    composable<Route.Tab.Home> {
        HomeScreen()
    }
}