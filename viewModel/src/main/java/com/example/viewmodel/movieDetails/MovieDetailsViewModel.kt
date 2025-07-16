package com.example.viewmodel.movieDetails

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NoInternetException
import com.example.domain.useCase.GetMovieDetailsUseCase
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.delay

class MovieDetailsViewModel(
    private val args: MovieDetailsArgs,
    private val movieDetailsUiStateMapper : MovieDetailsUiStateMapper,
    private val getMovieDetailsUseCase : GetMovieDetailsUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<MovieDetailsUiState, MovieDetailsEffect>(
    MovieDetailsUiState(),
    dispatcherProvider
), MovieDetailsInteractionListener {

    init {
        val movieId = args.movieId!!
        updateState { it.copy(movieId = movieId) }
        loadMovieDetails()
    }

    private fun loadMovieDetails(){
        updateState { it.copy(isLoading = true, networkError = false) }
        tryToExecute(
            action ={
                getMovieDetailsUseCase.invoke(state.value.movieId)},
            onSuccess = { movieDetails ->
                updateState { movieDetailsUiStateMapper.toUiState(movieDetails) }
            },
            onError = ::onError
        )
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

    override fun onShowAllCastClicked() {
        sendNewEffect(MovieDetailsEffect.NavigateToCastsScreenEffect)
    }

    override fun onBackClicked() {
        sendNewEffect(MovieDetailsEffect.NavigateBackEffect)
    }

    override fun onRetryQuestClicked() {
        loadMovieDetails()
    }

    private fun onError(exception: AflamiException) {
         when (exception) {
            is NoInternetException -> updateState { it.copy(isLoading = false , networkError = true) }
            else -> {}
        }
    }
}