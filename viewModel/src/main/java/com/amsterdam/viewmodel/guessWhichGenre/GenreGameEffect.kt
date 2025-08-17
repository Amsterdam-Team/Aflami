package com.amsterdam.viewmodel.guessWhichGenre

import com.amsterdam.viewmodel.gameResult.ResultScreenData

sealed interface GenreGameEffect {
    data object CancelGame : GenreGameEffect
    data class GameOver(
        val resultScreenData: ResultScreenData
    ) : GenreGameEffect
}
