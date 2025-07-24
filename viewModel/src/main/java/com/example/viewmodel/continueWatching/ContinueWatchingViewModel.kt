package com.example.viewmodel.continueWatching

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NoInternetException
import com.example.domain.useCase.GetContinueWatchingMoviesUseCase
import com.example.entity.Movie
import com.example.viewmodel.shared.BaseViewModel
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.flow.firstOrNull

class ContinueWatchingViewModel(
    private val getContinueWatchingMoviesUseCase: GetContinueWatchingMoviesUseCase,
    private val continueWatchingUiStateMapper: ContinueWatchingUiStateMapper,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<ContinueWatchingUiState, ContinueWatchingEffect>(
    ContinueWatchingUiState(),
    dispatcherProvider
),
    ContinueWatchingInteractionListener {

    init {
        getContinueWatchingMovies()
    }

    private fun getContinueWatchingMovies() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = ::loadContinueWatchingMovies,
            onSuccess = ::onGetContinueWatchingMoviesSuccess,
            onError = ::onError,
            onCompletion = ::onCompletion
        )
    }

    private suspend fun loadContinueWatchingMovies(): List<Movie> {
        return getContinueWatchingMoviesUseCase()
            .firstOrNull() ?: emptyList()
    }

    private fun onGetContinueWatchingMoviesSuccess(continueWatchingMovies: List<Movie>) {
        updateState { continueWatchingUiStateMapper.toUiState(continueWatchingMovies) }
    }

    private fun onError(exception: AflamiException) {
        when (exception) {
            is NoInternetException -> updateState {
                it.copy(
                    error = ContinueWatchingUiState.ContinueWatchingError.NetworkError
                )
            }
            else ->{}
        }
    }

    override fun onClickMovie(movieId: Long) {
        sendNewEffect(ContinueWatchingEffect.NavigateToMovieDetailsScreen(movieId))
    }

    override fun onClickRetryLoading() {
        getContinueWatchingMovies()
    }

    override fun onClickBack() {
        sendNewEffect(ContinueWatchingEffect.NavigateBack)
    }

    private fun onCompletion() = updateState { it.copy(isLoading = false) }
}