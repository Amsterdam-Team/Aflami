package com.example.ui.screens.letsPlay

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ui.navigation.Route
import com.example.ui.screens.profile.ProfileScreen

fun NavGraphBuilder.letsPlayScreenRoute(){
    composable<Route.Tab.LetsPlay> {
        LetsPlayScreen()
    }
}