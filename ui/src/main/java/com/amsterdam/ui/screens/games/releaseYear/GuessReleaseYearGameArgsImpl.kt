package com.amsterdam.ui.screens.games.releaseYear

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.amsterdam.ui.navigation.Route
import com.amsterdam.viewmodel.guessReleseDateGame.GuessReleaseYearGameArgs

class GuessReleaseYearGameArgsImpl(savedStateHandle: SavedStateHandle): GuessReleaseYearGameArgs {
    override val difficulty = savedStateHandle.toRoute<Route.GuessReleaseYearGame>().difficulty
}