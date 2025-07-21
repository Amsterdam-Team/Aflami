package com.example.viewmodel.home

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NoInternetException
import com.example.domain.useCase.GetPopularMoviesUseCase
import com.example.entity.Movie
import com.example.viewmodel.home.HomeUiState.HomeError
import com.example.viewmodel.shared.BaseViewModel
import com.example.viewmodel.utils.dispatcher.DispatcherProvider

class HomeViewModel(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val homeUiStateMapper: HomeUiStateMapper, dispatcherProvider: DispatcherProvider
) :
    BaseViewModel<HomeUiState, HomeEffect>(HomeUiState(), dispatcherProvider),
    HomeInteractionListener {

    init {
        getPopularMovies()
    }

    private fun getPopularMovies() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = { getPopularMoviesUseCase() },
            onSuccess = ::onGetPopularMovieSuccess,
            onError = ::onError
        )
    }

    private fun onGetPopularMovieSuccess(movies: List<Movie>) {
        updateState { homeUiStateMapper.toUiState(movies) }
    }

    override fun onClickRetryLoading() {
        getPopularMovies()
    }

    override fun onClickSearch() {
        sendNewEffect(HomeEffect.NavigateToSearchScreenEffect)
    }

    private fun onError(exception: AflamiException) {
        when (exception) {
            is NoInternetException -> updateState {
                it.copy(
                    isLoading = false,
                    error = HomeError.NetworkError
                )
            }

            else -> {}
        }
    }
}