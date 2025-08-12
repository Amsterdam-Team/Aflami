package com.amsterdam.ui.screens.game

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.gameScreenRoute() {
    composable<Route.Game> {
        GameScreen()
    }
}
