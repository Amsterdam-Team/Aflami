package com.example.viewmodel.movieDetails

import androidx.lifecycle.SavedStateHandle

class MovieDetailsArgs(savedStateHandle: SavedStateHandle){
    val movieId = savedStateHandle.get<Long>(MOVIE_ID_ARGS)

    companion object{
        const val MOVIE_ID_ARGS = "movieId"
    }
}