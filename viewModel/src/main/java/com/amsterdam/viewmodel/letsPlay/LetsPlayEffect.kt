package com.amsterdam.viewmodel.letsPlay

sealed interface LetsPlayEffect {
    object NavigateToGameScreen : LetsPlayEffect
}
