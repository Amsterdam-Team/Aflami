package com.amsterdam.viewmodel.gameEnd

data class ResultScreenData(
    val totalCollectedPoints: Int,
    val totalSpentSeconds: Int,
    val difficulty: String,
    val gameType: String
)