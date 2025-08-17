package com.amsterdam.ui.screens.letsPlay

import com.amsterdam.ui.R
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameDifficultyUiState.DifficultyLevelUiState

fun DifficultyLevelUiState.getDifficultyLevelTextId(): Int {
    return when(this){
        DifficultyLevelUiState.EASY -> R.string.easy
        DifficultyLevelUiState.MEDIUM -> R.string.medium
        DifficultyLevelUiState.HARD -> R.string.hard
    }
}