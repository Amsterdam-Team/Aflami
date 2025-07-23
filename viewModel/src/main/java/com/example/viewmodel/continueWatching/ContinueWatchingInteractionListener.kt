package com.example.viewmodel.continueWatching

interface ContinueWatchingInteractionListener {
    fun onClickMovie(movieId : Long)
    fun onClickBack()
    fun onClickRetryLoading()
}