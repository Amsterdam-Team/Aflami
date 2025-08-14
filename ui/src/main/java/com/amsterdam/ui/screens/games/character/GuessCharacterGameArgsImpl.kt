package com.amsterdam.ui.screens.games.character

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.amsterdam.ui.navigation.Route
import com.amsterdam.viewmodel.guessCharacterGame.GuessCharacterGameArgs

class GuessCharacterGameArgsImpl(savedStateHandle: SavedStateHandle): GuessCharacterGameArgs {
    override val difficulty = savedStateHandle.toRoute<Route.GuessCharacter>().difficulty
}