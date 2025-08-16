package com.amsterdam.ui.screens.letsPlay

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.amsterdam.ui.navigation.Route
import com.amsterdam.viewmodel.gameResult.GameResultArgs

class GameResultArgsImpl(savedStateHandle: SavedStateHandle): GameResultArgs {
    override val gameType = savedStateHandle.toRoute<Route.ResultScreen>().gameType
    override val  gameSessionId  = savedStateHandle.toRoute<Route.ResultScreen>().gameSessionId
    override val gameDifficulty = savedStateHandle.toRoute<Route.ResultScreen>().difficulty
}