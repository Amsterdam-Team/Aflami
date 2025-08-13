package com.amsterdam.viewmodel.guessCharacterGame


interface GuessCharacterGameEffect {
    object NavigateBack : GuessCharacterGameEffect
    data object NavigateToGame : GuessCharacterGameEffect
    data class NavigateToGameResult(val totalCollectedPoints: Int, val totalSpentSeconds: Int) :
        GuessCharacterGameEffect
}