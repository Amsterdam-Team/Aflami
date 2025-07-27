package com.amsterdam.viewmodel.home

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.models.Mood
import com.amsterdam.domain.useCase.home.GetContinueWatchingMoviesUseCase
import com.amsterdam.domain.useCase.home.GetMoviesByMoodUseCase
import com.amsterdam.domain.useCase.home.GetPopularMoviesUseCase
import com.amsterdam.domain.useCase.home.GetTopRatedMoviesUseCase
import com.amsterdam.domain.useCase.home.GetUpcomingMoviesUseCase
import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.home.HomeUiState.HomeError
import com.amsterdam.viewmodel.search.mapper.selectByMovieGenre
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.collections.get
import kotlin.text.get

class HomeViewModel(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getContinueWatchingMoviesUseCase: GetContinueWatchingMoviesUseCase,
    private val homeUiStateMapper: HomeUiStateMapper,
    private val getMoviesByMoodUseCase: GetMoviesByMoodUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel<HomeUiState, HomeEffect>(HomeUiState(), dispatcherProvider),
    HomeInteractionListener {

    init {
        getPopularMovies()
        getTopRatedMovies()
        getContinueWatchingMovies()
        getUpcomingMoviesBySelectedGenre()
    }

    private fun getPopularMovies() {
        updateState {
            it.copy(popularMoviesSectionUiState = it.popularMoviesSectionUiState.copy(isLoading = true))
        }
        tryToExecute(
            action = { getPopularMoviesUseCase() },
            onSuccess = ::onGetPopularMoviesSuccess,
            onError = ::onError,
        )
    }

    private fun onGetPopularMoviesSuccess(movies: List<Movie>) {
        updateState {
            it.copy(
                popularMoviesSectionUiState = it.popularMoviesSectionUiState.copy(
                    movies = homeUiStateMapper.moviesToPopularMoviesUiState(movies),
                    isLoading = false
                )
            )
        }
    }

    private fun getTopRatedMovies() {
        updateState {
            it.copy(
                topRatedMoviesSectionUiState = it.topRatedMoviesSectionUiState.copy(
                    isLoading = true
                )
            )
        }
        tryToExecute(
            action = { getTopRatedMoviesUseCase() },
            onSuccess = ::onGetTopRatedMoviesSuccess,
            onError = ::onError
        )
    }

    private fun onGetTopRatedMoviesSuccess(movies: List<Movie>) {
        updateState {
            it.copy(
                topRatedMoviesSectionUiState = it.topRatedMoviesSectionUiState.copy(
                    movies = homeUiStateMapper.moviesToMoviesItemsUiState(movies),
                    isLoading = false
                )
            )
        }
    }

    private fun getContinueWatchingMovies() {
        tryToExecute(
            action = { getContinueWatchingMoviesUseCase() },
            onSuccess = ::handleContinueWatchingMoviesFlow,
            onError = ::onError
        )
    }

    private fun handleContinueWatchingMoviesFlow(moviesFlow: Flow<List<Movie>>) {
        viewModelScope.launch(dispatcherProvider.IO) {
            moviesFlow.collect { movies ->
                updateState { currentState ->
                    currentState.copy(
                        continueWatchingMoviesSectionUiState = currentState.continueWatchingMoviesSectionUiState.copy(
                            movies = homeUiStateMapper.moviesToMoviesItemsUiState(
                                movies
                            )
                        ),
                    )
                }
            }
        }
    }

    private fun getUpcomingMoviesBySelectedGenre(selectedUpcomingGenre: MovieGenre = MovieGenre.ALL, isLoading: Boolean = true) {
        updateState {
            it.copy(
                upcomingMoviesSectionUiState = it.upcomingMoviesSectionUiState.copy(
                    isLoading = isLoading
                )
            )
        }
        tryToExecute(
            action = { getUpcomingMoviesUseCase(selectedUpcomingGenre) },
            onSuccess = ::onGetUpcomingMovieSuccess,
            onError = ::onError,
        )
    }

    private fun onGetUpcomingMovieSuccess(movies: List<Movie>) {
        updateState {
            it.copy(
                upcomingMoviesSectionUiState = it.upcomingMoviesSectionUiState.copy(
                    movies = homeUiStateMapper.moviesToMoviesItemsUiState(movies),
                    isLoading = false
                )
            )
        }
    }

    override fun onClickUpcomingMovieCard(id: Long) {
        sendNewEffect(HomeEffect.NavigateToMovieDetailsEffect(movieId = id))
    }

    override fun onChangeUpcomingMovieGenre(genre: MovieGenre) {
        if (genre == state.value.upcomingMoviesSectionUiState.getSelectedUpcomingMovieGenre()) return

        updateState {
            it.copy(
                upcomingMoviesSectionUiState = it.upcomingMoviesSectionUiState.copy(
                    movieGenres = it.upcomingMoviesSectionUiState.movieGenres.selectByMovieGenre(
                        genre
                    )
                )
            )
        }
        getUpcomingMoviesBySelectedGenre(selectedUpcomingGenre = genre, isLoading = false)
    }

    override fun onClickRetryLoading() {
        updateState { it.copy(error = null) }
        with(state.value) {
            if (popularMoviesSectionUiState.movies.isEmpty()) getPopularMovies()
            if (topRatedMoviesSectionUiState.movies.isEmpty()) getTopRatedMovies()
            if (upcomingMoviesSectionUiState.movies.isEmpty()) getUpcomingMoviesBySelectedGenre()
        }
    }

    override fun onClickSearch() {
        sendNewEffect(HomeEffect.NavigateToSearchScreenEffect)
    }

    override fun onClickMovie(movieId: Long) {
        sendNewEffect(HomeEffect.NavigateToMovieDetailsEffect(movieId))
    }

    override fun onClickShowAllContinueWatchingMovies() {
        sendNewEffect(HomeEffect.NavigateToContinueWatchingMoviesScreen)
    }

    override fun onClickShowAllToRatedMovies() {
        sendNewEffect(HomeEffect.NavigateToTopRatedMoviesEffect)
    }

    override fun onClickMood(mood: Mood) {
        updateState {
            it.copy(
                moodPickerUiState = it.moodPickerUiState.copy(
                    selectedMood = mood,
                    openMovieDialog = false
                )
            )
        }
    }

    override fun onClickGetNow() {
        updateState { it.copy(moodPickerUiState = it.moodPickerUiState.copy(isLoadingMovies = true)) }
        val selectedMood = state.value.moodPickerUiState.selectedMood ?: return
        tryToExecute(
            action = { getMoviesByMoodUseCase(selectedMood) },
            onSuccess = ::onGetMoviesByMoodSuccess,
            onError = ::onError
        )
    }

    override fun onDismissMoodPickerDialog() {
        updateState { it.copy(moodPickerUiState = it.moodPickerUiState.copy(openMovieDialog = false)) }
    }

    override fun onClickViewDetails() {
        onDismissMoodPickerDialog()
        sendNewEffect(HomeEffect.NavigateToMovieDetailsEffect(movieId = state.value.moodPickerUiState.selectedMovie.id))
    }

    override fun onClickGetAnotherMovie() {
        val currentMovieIndex = state.value.moodPickerUiState.movies.indexOf(
            state.value.moodPickerUiState.selectedMovie
        )
        val nextMovie: MovieItemUiState
        if (currentMovieIndex == state.value.moodPickerUiState.movies.size - 1) {
            nextMovie = state.value.moodPickerUiState.movies[0]
            return
        }
        nextMovie = state.value.moodPickerUiState.movies[currentMovieIndex + 1]
        updateState { it.copy(moodPickerUiState = it.moodPickerUiState.copy(selectedMovie = nextMovie)) }
    }

    private fun onGetMoviesByMoodSuccess(movies: List<Movie>) {
        if (movies.isEmpty()) {
            updateState { it.copy(moodPickerUiState = it.moodPickerUiState.copy(isLoadingMovies = false)) }
            return
        }
        val moviesUiStates = homeUiStateMapper.moviesToMoviesItemsUiState(movies)
        updateState {
            it.copy(
                moodPickerUiState = it.moodPickerUiState
                    .copy(
                        isLoadingMovies = false,
                        movies = moviesUiStates,
                        selectedMovie = moviesUiStates.getOrNull(0) ?: MovieItemUiState(),
                        openMovieDialog = true
                    )
            )
        }
    }

    private fun onError(exception: AflamiException) {
        when (exception) {
            is NetworkException -> updateState { it.copy(error = HomeError.NetworkError) }
            else -> {}
        }
    }
}