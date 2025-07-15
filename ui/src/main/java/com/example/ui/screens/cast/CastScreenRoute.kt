package com.example.ui.screens.cast

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ui.navigation.Route

fun NavGraphBuilder.castScreenRoute(){
    composable<Route.Cast> {
        CastScreen()
    }
}