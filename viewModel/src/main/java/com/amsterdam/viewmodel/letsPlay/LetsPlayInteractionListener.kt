package com.amsterdam.viewmodel.letsPlay

import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameDifficultyUiState
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameUiState.GameTypeUiState


interface LetsPlayInteractionListener {
    fun onSelectDifficultyLevel(difficultyLevel: GameDifficultyUiState)
    fun onClickCloseDifficultyLevelDialog()
    fun onClickGameCard(gameTypeUiState : GameTypeUiState)
    fun onClickStartGame()

}