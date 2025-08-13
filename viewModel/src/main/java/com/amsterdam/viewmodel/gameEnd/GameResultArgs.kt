package com.amsterdam.viewmodel.gameEnd

import androidx.lifecycle.SavedStateHandle

class GameResultArgs(savedStateHandle: SavedStateHandle) {
    val totalCollectedPoints = savedStateHandle.get<Int>(TOTAL_COLLECTED_POINTS_ARGS)
    val totalSpentSeconds = savedStateHandle.get<Int>(TOTAL_SPENT_SECONDS_ARGS)

    companion object {
        const val TOTAL_COLLECTED_POINTS_ARGS = "totalCollectedPoints"
        const val TOTAL_SPENT_SECONDS_ARGS = "totalSpentSeconds"
    }
}