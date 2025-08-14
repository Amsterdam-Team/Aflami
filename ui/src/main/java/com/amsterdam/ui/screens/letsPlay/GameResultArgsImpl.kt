package com.amsterdam.ui.screens.letsPlay

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.amsterdam.ui.navigation.Route
import com.amsterdam.viewmodel.gameEnd.GameResultArgs

class GameResultArgsImpl(savedStateHandle: SavedStateHandle): GameResultArgs {
    override val totalCollectedPoints = savedStateHandle.toRoute<Route.ResultScreen>().totalCollectedPoints
    override val totalSpentSeconds = savedStateHandle.toRoute<Route.ResultScreen>().totalSpentSeconds
    override val gameType = savedStateHandle.toRoute<Route.ResultScreen>().gameType
    override val gameDifficulty = savedStateHandle.toRoute<Route.ResultScreen>().difficulty
}