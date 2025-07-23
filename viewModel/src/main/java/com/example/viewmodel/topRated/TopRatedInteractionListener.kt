package com.example.viewmodel.topRated

interface TopRatedInteractionListener {
    fun onClickMovie(movieId : Long)
    fun onClickBack()
    fun onClickRetryLoading()
}