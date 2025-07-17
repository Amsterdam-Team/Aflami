package com.example.viewmodel.movieDetails

interface MovieDetailsInteractionListener  {
    fun onMovieExtrasClicked(movieExtras: MovieExtras)
    fun onShowAllCastClick()
    fun onBackClick()
    fun onRetryQuestClick()
}