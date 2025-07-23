package com.example.viewmodel.home

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NetworkException
import com.example.domain.useCase.GetUpcomingMoviesUseCase
import com.example.entity.Movie
import com.example.entity.category.MovieGenre
import com.example.domain.useCase.GetHomeScreenDataUseCase
import com.example.domain.useCase.GetHomeScreenDataUseCase.HomeScreenData
import com.example.viewmodel.home.HomeUiState.HomeError
import com.example.viewmodel.search.mapper.selectByMovieGenre
import com.example.viewmodel.shared.BaseViewModel
import com.example.viewmodel.utils.dispatcher.DispatcherProvider

class HomeViewModel(
    private val getHomeScreenDataUseCase: GetHomeScreenDataUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val homeUiStateMapper: HomeUiStateMapper, dispatcherProvider: DispatcherProvider
) :
    BaseViewModel<HomeUiState, HomeEffect>(HomeUiState(), dispatcherProvider),
    HomeInteractionListener {

    init {
        getHomeScreenData()
        getUpcomingMoviesBySelectedGenre()
    }

    private fun getHomeScreenData() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = { getHomeScreenDataUseCase() },
            onSuccess = ::onGetHomeScreenDataSuccess,
            onError = ::onError,
            onCompletion = ::onCompletion
        )
    }

    fun onGetHomeScreenDataSuccess(homeScreenData: HomeScreenData){
        updateState { homeUiStateMapper.toUiState(homeScreenData) }
    }

    override fun onClickRetryLoading() {
        updateState { it.copy(error = null) }
        getHomeScreenData()
        getUpcomingMoviesBySelectedGenre()
    }

    override fun onClickSearch() {
        sendNewEffect(HomeEffect.NavigateToSearchScreenEffect)
    }

    override fun onClickMovie(movieId: Long) {
        sendNewEffect(HomeEffect.NavigateToMovieDetailsEffect(movieId))
    }

    override fun onClickShowAllToRatedMovies() {
        sendNewEffect(HomeEffect.NavigateToTopRatedMoviesEffect)
    }

    private fun getUpcomingMoviesBySelectedGenre(selectedUpcomingGenre: MovieGenre = MovieGenre.ALL) {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = { getUpcomingMoviesUseCase(selectedUpcomingGenre) },
            onSuccess = ::onGetUpcomingMovieSuccess,
            onError = ::onError,
            onCompletion = ::onCompletion
        )
    }

    private fun onGetUpcomingMovieSuccess(movies: List<Movie>) {
        updateState { it.copy(upcomingMovies = homeUiStateMapper.moviesToMoviesItemsUiState(movies)) }
    }

    override fun onClickUpcomingMovieCard(id: Long) {
        sendNewEffect(HomeEffect.NavigateToMovieDetailsEffect(movieId = id))
    }

    override fun onChangeUpcomingMovieGenre(genre: MovieGenre) {
        if (genre == state.value.getSelectedUpcomingMovieGenre()) return

        updateState { it.copy(upcomingMovieGenres = it.upcomingMovieGenres.selectByMovieGenre(genre)) }
        getUpcomingMoviesBySelectedGenre(selectedUpcomingGenre = genre)
    }

    private fun onError(exception: AflamiException) {
        when (exception) {
            is NetworkException -> updateState { it.copy(error = HomeError.NetworkError) }
            else -> {}
        }
    }

    private fun onCompletion() = updateState { it.copy(isLoading = false) }
}