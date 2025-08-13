package com.amsterdam.viewmodel.guessReleseDateGame

import androidx.lifecycle.SavedStateHandle

class  GuessReleaseYearGameArgs (savedStateHandle: SavedStateHandle) {

    val difficulty: String = savedStateHandle.get<String>(DIFFICULTY_ARGS)!!

    companion object {
        const val DIFFICULTY_ARGS = "difficulty"
    }
}
