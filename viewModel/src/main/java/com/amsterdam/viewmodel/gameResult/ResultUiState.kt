package com.amsterdam.viewmodel.gameResult

import com.amsterdam.entity.Game

data class ResultUiState(
    val gameType: Game.GameType = Game.GameType.GUESS_CHARACTER,
    val points: Int = 0,
    val timeInSeconds: Int = 0
)