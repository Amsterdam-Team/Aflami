package com.amsterdam.viewmodel.gameResult

import com.amsterdam.entity.GameDifficulty
import com.amsterdam.viewmodel.shared.BaseViewModel

sealed interface ResultSideEffect : BaseViewModel.BaseUiEffect {
    data class NavigateToGame(
         val totalCollectedPoints: Int,
         val totalSpentSeconds: Int,val gameType: GameType, val difficulty: GameDifficulty
    ) : ResultSideEffect

    data object NavigateBackToMenu : ResultSideEffect


    enum class GameType {
        GUESS_MOVIE_BY_POSTER,
        GUESS_RELEASE_YEAR,
        GUESS_CHARACTER,
        GUESS_GENRE
    }
}