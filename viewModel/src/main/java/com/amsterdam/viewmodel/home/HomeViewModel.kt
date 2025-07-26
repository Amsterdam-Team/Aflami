package com.amsterdam.viewmodel.home

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.models.Mood
import com.amsterdam.domain.useCase.home.GetContinueWatchingMoviesUseCase
import com.amsterdam.domain.useCase.home.GetContinueWatchingTvShowsUseCase
import com.amsterdam.domain.useCase.home.GetHomeScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetHomeScreenDataUseCase.HomeScreenData
import com.amsterdam.domain.useCase.home.GetMoviesByMoodUseCase
import com.amsterdam.domain.useCase.home.GetUpcomingMoviesUseCase
import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.home.HomeUiState.HomeError
import com.amsterdam.viewmodel.search.mapper.selectByMovieGenre
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getHomeScreenDataUseCase: GetHomeScreenDataUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getContinueWatchingMoviesUseCase: GetContinueWatchingMoviesUseCase,
    private val getContinueWatchingTvShowsUseCase: GetContinueWatchingTvShowsUseCase,
    private val homeUiStateMapper: HomeUiStateMapper,
    private val getMoviesByMoodUseCase: GetMoviesByMoodUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel<HomeUiState, HomeEffect>(HomeUiState(), dispatcherProvider),
    HomeInteractionListener {

    init {
        observeContinueWatchingMovies()
        observeContinueWatchingTvShows()
        getHomeScreenData()
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

    fun onGetHomeScreenDataSuccess(homeScreenData: HomeScreenData) {
        updateState { homeUiStateMapper.toUiState(homeScreenData) }
    }

    override fun onClickRetryLoading() {
        updateState { it.copy() }
        getHomeScreenData()
    }

    override fun onClickSearch() {
        sendNewEffect(HomeEffect.NavigateToSearchScreenEffect)
    }

    override fun onClickMediaItem(mediaId: Long, mediaType: MediaType) {
        if (mediaType == MediaType.MOVIE)
            sendNewEffect(HomeEffect.NavigateToMovieDetailsEffect(mediaId))
        else
            sendNewEffect(HomeEffect.NavigateToTvShowDetailsEffect(mediaId))

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

    private fun observeContinueWatchingMovies() {
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
                        continueWatchingItems = (currentState.continueWatchingItems + movies.map {
                            homeUiStateMapper.movieToMediaItemUiState(it)
                        }).toSet().toList().reversed()
                    )
                }
            }
        }
    }

    private fun observeContinueWatchingTvShows() {
        tryToExecute(
            action = { getContinueWatchingTvShowsUseCase() },
            onSuccess = ::handleContinueWatchingTvShowsFlow,
            onError = ::onError
        )
    }

    private fun handleContinueWatchingTvShowsFlow(tvShowsFlow: Flow<List<TvShow>>) {
        viewModelScope.launch(dispatcherProvider.IO) {
            tvShowsFlow.collect { tvShows ->
                updateState { currentState ->
                    currentState.copy(
                        continueWatchingItems = (currentState.continueWatchingItems + tvShows.map {
                            homeUiStateMapper.tvShowToMediaItemUiState(it)
                        }).toSet().toList().reversed()
                    )
                }
            }
        }
    }

    private fun onError(exception: AflamiException) {
        when (exception) {
            is NetworkException -> updateState { it.copy(error = HomeError.NetworkError) }
            else -> {}
        }
    }

    private fun onCompletion() = updateState { it.copy() }
}