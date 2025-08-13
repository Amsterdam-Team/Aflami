package com.amsterdam.ui.screens.games.releaseYear

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.guessReleaseYearScreenScreenRoute(){
    composable<Route.GuessReleaseYearGame> {
        GuessReleaseYearScreen()
    }
}