package com.amsterdam.viewmodel.gameResult

data class ResultScreenData(
    val totalCollectedPoints: Int,
    val totalSpentSeconds: Int,
    val difficulty: String,
    val gameType: String
)