package com.amsterdam.ui.screens.games.guessGenre

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.amsterdam.ui.navigation.Route
import com.amsterdam.viewmodel.guessWhichGenre.GameGenreArgs

class GameGenreArgsImpl(savedStateHandle: SavedStateHandle): GameGenreArgs {
    override val difficulty = savedStateHandle.toRoute<Route.GenreGame>().difficulty
}