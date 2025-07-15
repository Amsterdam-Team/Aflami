package com.example.viewmodel.movieDetails

import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.common.Selectable
import com.example.viewmodel.utils.dispatcher.DispatcherProvider

class MovieDetailsViewModel(
    private val args: MovieDetailsArgs,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<MovieDetailsUiState, MovieDetailsEffect>(
    MovieDetailsUiState(),
    dispatcherProvider
),
    MovieDetailsInteractionListener {

    init {
        updateState { it.copy(movieId = args.movieId!!) }
    }

    override fun onMovieExtrasClicked(movieExtras: MovieExtras) {
        updateState { state ->
            state.copy(
                extraItem = state.extraItem.map { selectable ->
                    selectable.copy(isSelected = selectable.item == movieExtras)
                }
            )
        }
    }

}