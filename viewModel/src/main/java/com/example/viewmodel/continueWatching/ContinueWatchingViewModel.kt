package com.example.viewmodel.continueWatching

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NoInternetException
import com.example.domain.useCase.GetContinueWatchingMoviesUseCase
import com.example.entity.Movie
import com.example.viewmodel.shared.BaseViewModel
import com.example.viewmodel.utils.dispatcher.DispatcherProvider

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
            action = { getContinueWatchingMoviesUseCase() },
            onSuccess = ::onGetContinueWatchingMoviesSuccess,
            onError = ::onError
        )
    }

    private fun onGetContinueWatchingMoviesSuccess(continueWatchingMovies: List<Movie>) {
        updateState { continueWatchingUiStateMapper.toUiState(continueWatchingMovies) }
    }

    private fun onError(exception: AflamiException) {
        when (exception) {
            is NoInternetException -> updateState {
                it.copy(
                    isLoading = false,
                    error = ContinueWatchingUiState.ContinueWatchingError.NetworkError
                )
            }
            else ->
                updateState {
                    it.copy(
                        isLoading = false,
                    )
                }
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


}