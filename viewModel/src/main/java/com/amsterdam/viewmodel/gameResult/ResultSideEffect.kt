package com.amsterdam.viewmodel.gameResult

import com.amsterdam.entity.Game
import com.amsterdam.viewmodel.shared.BaseViewModel

sealed interface ResultSideEffect : BaseViewModel.BaseUiEffect {
    data class NavigateToGame(
        val gameType : Game.GameType,
       val difficultyType : String
    ) : ResultSideEffect

    data object NavigateBackToMenu : ResultSideEffect
}