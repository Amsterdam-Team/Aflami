package com.amsterdam.ui.screens.games.guessByPoster

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.guessByPosterGameScreenRoute() {
    composable<Route.GuessMovieByPosterGame> {
        GuessByPosterGameScreen()
    }
}