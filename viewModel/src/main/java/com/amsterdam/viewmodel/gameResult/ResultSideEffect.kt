package com.amsterdam.viewmodel.gameResult

import com.amsterdam.viewmodel.shared.BaseViewModel

sealed interface ResultSideEffect : BaseViewModel.BaseUiEffect {
    data class NavigateToGame(
        val gameType : GameTypeUi,
       val difficultyType : String
    ) : ResultSideEffect

    data object NavigateBackToMenu : ResultSideEffect

    enum class GameTypeUi {
        GUESS_MOVIE_BY_POSTER,
        GUESS_RELEASE_YEAR,
        GUESS_CHARACTER,
        GUESS_GENRE
    }
}