package com.amsterdam.viewmodel.guessCharacterGame

import com.amsterdam.viewmodel.gameResult.ResultScreenData


interface GuessCharacterGameEffect {
    object NavigateBack : GuessCharacterGameEffect
    data class NavigateToGameResult(val resultScreenData : ResultScreenData) :
        GuessCharacterGameEffect
}