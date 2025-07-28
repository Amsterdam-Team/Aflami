package com.amsterdam.viewmodel.home

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.models.Mood
import com.amsterdam.domain.useCase.home.GetContinueWatchingScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetContinueWatchingScreenDataUseCase.ContinueWatchingScreenData
import com.amsterdam.domain.useCase.home.GetHomeScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetHomeScreenDataUseCase.HomeScreenData
import com.amsterdam.domain.useCase.home.GetMoviesByMoodUseCase
import com.amsterdam.domain.useCase.home.GetUpcomingMoviesUseCase
import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.home.HomeUiState.HomeError
import com.amsterdam.viewmodel.search.mapper.selectByMovieGenre
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import com.amsterdam.viewmodel.utils.getLinearItemsList
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeScreenDataUseCase: GetHomeScreenDataUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getContinueWatchingScreenDataUseCase: GetContinueWatchingScreenDataUseCase,
    private val homeUiStateMapper: HomeUiStateMapper,
    private val getMoviesByMoodUseCase: GetMoviesByMoodUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel<HomeUiState, HomeEffect>(HomeUiState(), dispatcherProvider),
    HomeInteractionListener {

    init {
        getContinueWatchingData()
        getHomeScreenData()
    }

    private fun getHomeScreenData() {
        updateState { it.copy(
            isLoading = true,
            popularMediaSectionUiState = state.value.popularMediaSectionUiState.copy(isLoading = true),
            topRatedMediaSectionUiState = state.value.topRatedMediaSectionUiState.copy(isLoading = true),
            upcomingMoviesSectionUiState = state.value.upcomingMoviesSectionUiState.copy(isLoading = true),
            continueWatchingMediaSectionUiState = state.value.continueWatchingMediaSectionUiState.copy(isLoading = true),
        ) }
        tryToExecute(
            action = { getHomeScreenDataUseCase() },
            onSuccess = ::onGetHomeScreenDataSuccess,
            onError = ::onError,
        )
    }

    fun onGetHomeScreenDataSuccess(homeScreenData: HomeScreenData) {
        updateState {
            homeUiStateMapper.toUiState(
                homeScreenData,
                state.value.continueWatchingMediaSectionUiState.mediaItems
            )
        }
    }

    private fun getContinueWatchingData() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = { getContinueWatchingScreenDataUseCase() },
            onSuccess = ::onGetContinueWatchingScreenDataSuccess,
            onError = ::onError,
            onCompletion = ::onCompletion
        )
    }

    fun onGetContinueWatchingScreenDataSuccess(continueWatchingData: ContinueWatchingScreenData) {
        combine(
            continueWatchingData.continueWatchingMovies,
            continueWatchingData.continueWatchingTvShows
        ) { movies, tvShows ->
            updateState { currentState ->
                currentState.copy(
                    continueWatchingMediaSectionUiState = currentState.continueWatchingMediaSectionUiState.copy(
                        mediaItems = getLinearItemsList(
                            movies,
                            tvShows,
                            homeUiStateMapper::movieToMediaItemUiState,
                            homeUiStateMapper::tvShowToMediaItemUiState
                        )
                    )
                )
            }
        }.launchIn(viewModelScope)
    }


    private fun getUpcomingMoviesBySelectedGenre(
        selectedUpcomingGenre: MovieGenre = MovieGenre.ALL,
        isLoading: Boolean = true
    ) {
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
            if (popularMediaSectionUiState.mediaItems.isEmpty()) getHomeScreenData()
            if (topRatedMediaSectionUiState.mediaItems.isEmpty()) getHomeScreenData()
            if (upcomingMoviesSectionUiState.movies.isEmpty()) getUpcomingMoviesBySelectedGenre()
        }
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

    override fun onClickUpcomingMovieCard(id: Long) {
        sendNewEffect(HomeEffect.NavigateToMovieDetailsEffect(movieId = id))
    }

    private fun onError(exception: AflamiException) {
        when (exception) {
            is NetworkException -> updateState { it.copy(error = HomeError.NetworkError) }
            else -> {}
        }
    }

    private fun onCompletion() = updateState { it.copy() }
}