package com.example.viewmodel.movieDetails

import com.example.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras

interface MovieDetailsInteractionListener  {
    fun onClickMovieExtras(movieExtras: MovieExtras)
    fun onClickShowAllCast()
    fun onClickBack()
    fun onClickRetryRequest()
}