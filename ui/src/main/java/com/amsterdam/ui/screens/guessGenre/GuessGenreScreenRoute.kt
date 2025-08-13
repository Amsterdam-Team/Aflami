package com.amsterdam.ui.screens.guessGenre

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.guessGenreScreenRoute() {
    composable<Route.GenreGame> {
        GuessGenreScreen()
    }
}
