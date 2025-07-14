package com.example.viewmodel.movieDetails

import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.utils.dispatcher.DispatcherProvider

class MovieDetailsViewModel(
    private val args: MovieDetailsArgs,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<MovieDetailsUiState, MovieDetailsEffect>(MovieDetailsUiState(),dispatcherProvider){

init {
    updateState{ it.copy(movieId = args.movieId!!)}
}
}