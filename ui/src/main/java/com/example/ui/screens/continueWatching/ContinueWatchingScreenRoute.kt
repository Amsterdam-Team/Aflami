package com.example.ui.screens.continueWatching

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ui.navigation.Route

fun NavGraphBuilder.continueWatchingScreenRoute(){
    composable<Route.ContinueWatching> {
        ContinueWatchingScreen()
    }
}