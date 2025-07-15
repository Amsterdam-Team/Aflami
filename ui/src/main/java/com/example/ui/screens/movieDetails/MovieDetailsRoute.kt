package com.example.ui.screens.movieDetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ui.navigation.Route

fun NavGraphBuilder.movieDetailsScreenRoute(){
    composable<Route.MovieDetails> {
        MovieDetailsScreen()
    }
}