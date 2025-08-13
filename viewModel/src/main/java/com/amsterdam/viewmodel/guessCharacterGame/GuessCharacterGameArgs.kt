package com.amsterdam.viewmodel.guessCharacterGame

import androidx.lifecycle.SavedStateHandle

class GuessCharacterGameArgs (savedStateHandle: SavedStateHandle) {

    val difficulty: String = savedStateHandle.get<String>(DIFFICULTY_ARGS)!!

    companion object {
        const val DIFFICULTY_ARGS = "difficulty"
    }
}
