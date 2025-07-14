package com.example.ui.screens.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ui.navigation.Route

fun NavGraphBuilder.profileScreenRoute() {
    composable<Route.Tab.Profile> {
        ProfileScreen()
    }
}