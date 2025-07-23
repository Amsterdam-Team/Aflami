package com.example.viewmodel.topRated

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NoInternetException
import com.example.domain.useCase.GetTopRatedMoviesUseCase
import com.example.entity.Movie
import com.example.viewmodel.shared.BaseViewModel
import com.example.viewmodel.topRated.TopRatedUiState.TopRatedError
import com.example.viewmodel.utils.dispatcher.DispatcherProvider

class TopRatedViewModel(
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val topRatedUiStateMapper: TopRatedUiStateMapper,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<TopRatedUiState, TopRatedEffect>(TopRatedUiState(), dispatcherProvider),
    TopRatedInteractionListener {

    init {
        getTopRatedMovies()
    }

    private fun getTopRatedMovies() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = { getTopRatedMoviesUseCase() },
            onSuccess = ::onGetTopRatedMoviesSuccess,
            onError = ::onError
        )
    }

    private fun onGetTopRatedMoviesSuccess(topRatedMovies: List<Movie>) {
        updateState { topRatedUiStateMapper.toUiState(topRatedMovies) }
    }

    private fun onError(exception: AflamiException) {
        when (exception) {
            is NoInternetException -> updateState {
                it.copy(
                    isLoading = false,
                    error = TopRatedError.NetworkError
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
        sendNewEffect(TopRatedEffect.NavigateToMovieDetailsScreen(movieId))
    }

    override fun onClickRetryLoading() {
        getTopRatedMovies()
    }

    override fun onClickBack() {
        sendNewEffect(TopRatedEffect.NavigateBack)
    }
}