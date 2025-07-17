package com.example.viewmodel.cast

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NoInternetException
import com.example.domain.useCase.GetMovieCastUseCase
import com.example.entity.Actor
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.cast.mapper.toUiState
import com.example.viewmodel.movieDetails.MovieDetailsArgs
import com.example.viewmodel.utils.dispatcher.DispatcherProvider

class CastViewModel(
    private val getMovieCastUseCase: GetMovieCastUseCase,
    private val args: MovieDetailsArgs,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<CastUiState, CastUiEffect>(CastUiState(), dispatcherProvider),
    CastInteractionListener {

    init { fetchMovieCast() }

    private fun fetchMovieCast() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = ::executeFetchMovieCast,
            onSuccess = ::onGetMovieCastSuccess,
            onError = ::onGetMovieCastError,
            onCompletion = ::onGetMovieCastCompletion,
        )
    }

    private suspend fun executeFetchMovieCast() =
        getMovieCastUseCase(args.movieId!!)

    private fun onGetMovieCastSuccess(cast: List<Actor>) {
        updateState { it.copy(cast = cast.map { it.toUiState() }, errorUiState = null) }
    }

    private fun onGetMovieCastError(exception: AflamiException) {
        val errorUiState = when (exception) {
            is NoInternetException -> CastErrorUiState.NoNetworkConnection
            else -> null
        }

        updateState { it.copy(errorUiState = errorUiState) }
    }

    private fun onGetMovieCastCompletion() = updateState { it.copy(isLoading = false) }

    override fun onNavigateBackClicked() = sendNewEffect(CastUiEffect.NavigateBack)

    override fun onRetryQuestClicked() = fetchMovieCast()

}