package com.amsterdam.viewmodel.gameResult


interface GameResultArgs {
    val totalCollectedPoints: Int
    val totalSpentSeconds: Int
    val gameType: String
    val gameDifficulty: String
}