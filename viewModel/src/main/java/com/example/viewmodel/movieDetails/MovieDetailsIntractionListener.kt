package com.example.viewmodel.movieDetails

import com.example.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras

interface MovieDetailsInteractionListener  {
    fun onMovieExtrasClicked(movieExtras: MovieExtras)
    fun onShowAllCastClick()
    fun onBackClick()
    fun onRetryQuestClick()
}