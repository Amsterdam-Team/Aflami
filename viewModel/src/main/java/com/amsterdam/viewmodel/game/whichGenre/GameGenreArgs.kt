package com.amsterdam.viewmodel.game.whichGenre

import androidx.lifecycle.SavedStateHandle
import com.amsterdam.entity.GameDifficulty

class GameGenreArgs(
    savedStateHandle: SavedStateHandle,
) {
    val difficulty: String = savedStateHandle.get<String>(DIFFICULTY)
        ?: GameDifficulty.DifficultyType.EASY.name

    companion object {
        private const val DIFFICULTY = "difficulty"
    }
}