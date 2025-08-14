package com.amsterdam.viewmodel.guessMovieByPosterGame

import com.amsterdam.viewmodel.gameResult.ResultScreenData

interface GuessMovieByPosterGameEffect {
    object NavigateBack : GuessMovieByPosterGameEffect
    data class NavigateToGameResult(val resultScreenData : ResultScreenData) :
        GuessMovieByPosterGameEffect
}