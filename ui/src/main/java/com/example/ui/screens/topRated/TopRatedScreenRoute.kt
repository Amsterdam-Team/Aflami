package com.example.ui.screens.topRated

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ui.navigation.Route

fun NavGraphBuilder.topRatedScreenRoute(){
    composable<Route.TopRated> {
        TopRatedScreen()
    }
}