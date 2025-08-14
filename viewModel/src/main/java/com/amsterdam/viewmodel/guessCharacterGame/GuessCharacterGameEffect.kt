package com.amsterdam.viewmodel.guessCharacterGame

import com.amsterdam.viewmodel.gameEnd.ResultScreenData


interface GuessCharacterGameEffect {
    object NavigateBack : GuessCharacterGameEffect
    data object NavigateToGame : GuessCharacterGameEffect
    data class NavigateToGameResult(val resultScreenData : ResultScreenData) :
        GuessCharacterGameEffect
}