package com.amsterdam.viewmodel.gameEnd


interface GameResultArgs {
    val totalCollectedPoints: Int
    val totalSpentSeconds: Int
    val gameType: String
    val gameDifficulty: String
}