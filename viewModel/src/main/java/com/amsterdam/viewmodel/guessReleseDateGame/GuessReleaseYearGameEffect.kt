package com.amsterdam.viewmodel.guessReleseDateGame

import com.amsterdam.viewmodel.gameResult.ResultScreenData

interface GuessReleaseYearGameEffect {
    object NavigateBack : GuessReleaseYearGameEffect
    data class NavigateToGameResult(val resultScreenData : ResultScreenData) : GuessReleaseYearGameEffect
}
