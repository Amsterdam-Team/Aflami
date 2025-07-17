package com.example.viewmodel.movieDetails

interface MovieDetailsInteractionListener  {
    fun onMovieExtrasClicked(movieExtras: MovieExtras)
    fun onShowAllCastClicked()
    fun onBackClicked()
    fun onRetryQuestClicked()
}