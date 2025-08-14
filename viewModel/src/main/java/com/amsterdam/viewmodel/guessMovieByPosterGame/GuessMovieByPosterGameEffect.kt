package com.amsterdam.viewmodel.guessMovieByPosterGame

import com.amsterdam.viewmodel.gameEnd.ResultScreenData

interface GuessMovieByPosterGameEffect {
    object NavigateBack : GuessMovieByPosterGameEffect
    data class NavigateToGameResult(val resultScreenData : ResultScreenData) :
        GuessMovieByPosterGameEffect
}