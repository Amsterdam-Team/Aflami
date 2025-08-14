package com.amsterdam.ui.screens.movieDetails

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.amsterdam.ui.navigation.Route
import com.amsterdam.viewmodel.movieDetails.MovieDetailsArgs

class MovieDetailsArgsImpl(savedStateHandle: SavedStateHandle): MovieDetailsArgs {
    override val movieId = savedStateHandle.toRoute<Route.MovieDetails>().movieId
}