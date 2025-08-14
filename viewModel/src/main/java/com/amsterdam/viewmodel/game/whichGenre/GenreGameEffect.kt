package com.amsterdam.viewmodel.game.whichGenre

import com.amsterdam.viewmodel.gameEnd.ResultScreenData

sealed interface GenreGameEffect {
    data object CancelGame : GenreGameEffect
    data class GameOver(
        val resultScreenData : ResultScreenData
    ) : GenreGameEffect
    data object ShowNotEnoughPointsSnackBar: GenreGameEffect
}
