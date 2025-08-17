package com.amsterdam.viewmodel.letsPlay

import com.amsterdam.entity.Game
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameDifficultyUiState

interface LetsPlayInteractionListener {
    fun onSelectDifficultyLevel(difficultyLevel: GameDifficultyUiState)
    fun onClickCloseDifficultyLevelDialog()
    fun onClickGameCard(gameType : Game.GameType)
    fun onClickStartGame()
}