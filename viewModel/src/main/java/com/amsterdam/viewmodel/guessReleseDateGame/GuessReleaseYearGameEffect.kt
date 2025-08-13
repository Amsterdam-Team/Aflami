package com.amsterdam.viewmodel.guessReleseDateGame


interface GuessReleaseYearGameEffect {
    object NavigateBack : GuessReleaseYearGameEffect
    data class NavigateToGameResult(val totalCollectedPoints : Int , val totalSpentSeconds : Int) : GuessReleaseYearGameEffect
}