package com.amsterdam.viewmodel.game.whichGenre

sealed interface GenreGameEffect {
    object CancelGame : GenreGameEffect
    data class GameOver(
        val totalEarnedPoints: Int,
        val answersTotalTime: Int
    ) : GenreGameEffect
    object ShowNotEnoughPointsSnackBar: GenreGameEffect
}
