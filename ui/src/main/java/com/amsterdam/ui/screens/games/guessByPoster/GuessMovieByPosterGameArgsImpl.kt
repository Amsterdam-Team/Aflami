package com.amsterdam.ui.screens.games.guessByPoster

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.amsterdam.ui.navigation.Route
import com.amsterdam.viewmodel.guessMovieByPosterGame.GuessMovieByPosterGameArgs

class GuessMovieByPosterGameArgsImpl(savedStateHandle: SavedStateHandle): GuessMovieByPosterGameArgs {
    override val difficulty = savedStateHandle.toRoute<Route.GuessMovieByPosterGame>().difficulty
}