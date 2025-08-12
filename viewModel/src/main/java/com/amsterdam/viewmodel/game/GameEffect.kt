package com.amsterdam.viewmodel.game

sealed interface GameEffect {
    object CancelGame : GameEffect

    object GameOver : GameEffect
}
