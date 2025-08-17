package com.amsterdam.viewmodel.home

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.utils.Mood
import com.amsterdam.domain.useCase.home.GetContinueWatchingScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetContinueWatchingScreenDataUseCase.ContinueWatchingScreenData
import com.amsterdam.domain.useCase.home.GetHomeScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetHomeScreenDataUseCase.HomeScreenData
import com.amsterdam.domain.useCase.home.GetMoviesByMoodUseCase
import com.amsterdam.domain.useCase.home.GetUpcomingMoviesUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.entity.Movie
import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.entity.TvShowWatchHistory
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.home.HomeUiState.HomeError
import com.amsterdam.viewmodel.home.HomeUiState.MoodPickerItemUiState
import com.amsterdam.viewmodel.search.mapper.selectByMovieGenre
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.MediaType

import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import com.amsterdam.viewmodel.utils.getLinearItemsList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeScreenDataUseCase: GetHomeScreenDataUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getContinueWatchingScreenDataUseCase: GetContinueWatchingScreenDataUseCase,
    private val getMoviesByMoodUseCase: GetMoviesByMoodUseCase,
    private val manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel<HomeUiState, HomeEffect>(HomeUiState(), dispatcherProvider),
    HomeInteractionListener {

    init {
        showHomeScreenDataLoading()
        manageLocaleLanguageUseCase.getAppLanguage()
            .onEach {
                getHomeScreenData()
                getContinueWatchingData()
            }.launchIn(viewModelScope)
    }

    private fun getHomeScreenData() {
        tryToExecute(
            action = { getHomeScreenDataUseCase() },
            onSuccess = ::onGetHomeScreenDataSuccess,
            onError = ::onError,
            onCompletion = ::onCompletion
        )
    }

    private fun showHomeScreenDataLoading() {
        updateState {
            it.copy(
                isLoading = true,
                popularMediaSectionUiState = state.value.popularMediaSectionUiState.copy(isLoading = true),
                topRatedMediaSectionUiState = state.value.topRatedMediaSectionUiState.copy(isLoading = true),
                upcomingMoviesSectionUiState = state.value.upcomingMoviesSectionUiState.copy(
                    isLoading = true
                ),
                continueWatchingMediaSectionUiState = state.value.continueWatchingMediaSectionUiState.copy(
                    isLoading = true
                ),
            )
        }
    }

    private fun onGetHomeScreenDataSuccess(homeScreenData: HomeScreenData) {
        updateState {
            homeScreenData.toHomeUiState(state.value.continueWatchingMediaSectionUiState.mediaItems)
        }
    }

    private fun getContinueWatchingData() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = { getContinueWatchingScreenDataUseCase(pageSize = 10) },
            onSuccess = ::onGetContinueWatchingScreenDataSuccess,
            onError = ::onError,
            onCompletion = ::onCompletion
        )
    }

    private fun onGetContinueWatchingScreenDataSuccess(continueWatchingData: Flow<ContinueWatchingScreenData>) {
        continueWatchingData.onEach {
            updateState { currentState ->
                currentState.copy(
                    continueWatchingMediaSectionUiState = currentState.continueWatchingMediaSectionUiState.copy(
                        mediaItems = getLinearItemsList(
                            it.continueWatchingMovies,
                            it.continueWatchingTvShows,
                            MovieWatchHistory::toContinueWatchingMediaItemUiState,
                            TvShowWatchHistory::toContinueWatchingMediaItemUiState
                        ).sortedByDescending { it.dateAdded }
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
                    movies = movies.map { it.toUpcomingMediaItemUiState() },
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
            if (continueWatchingMediaSectionUiState.mediaItems.isEmpty()) getContinueWatchingData()
        }
    }

    override fun onClickSearch() {
        sendNewNavigationEffect(HomeEffect.NavigateToSearchScreenEffect)
    }

    override fun onClickMediaItem(mediaId: Long, mediaType: MediaType) {
        if (mediaType == MediaType.MOVIE)
            sendNewNavigationEffect(HomeEffect.NavigateToMovieDetailsEffect(mediaId))
        else
            sendNewNavigationEffect(HomeEffect.NavigateToTvShowDetailsEffect(mediaId))
    }

    override fun onClickShowAllContinueWatchingMovies() {
        sendNewNavigationEffect(HomeEffect.NavigateToContinueWatchingMoviesScreen)
    }

    override fun onClickShowAllToRatedMovies() {
        sendNewNavigationEffect(HomeEffect.NavigateToTopRatedMoviesEffect)
    }

    override fun onChangeMood(mood: Mood) {
        updateState {
            it.copy(
                moodPickerUiState = it.moodPickerUiState.copy(
                    selectedMood = mood,
                    isLoadingMovies = false,
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
            onError = ::onGetMoviesByMoodError
        )
    }

    private fun onGetMoviesByMoodSuccess(movies: List<Movie>) {
        if (movies.isEmpty()) {
            updateState { it.copy(moodPickerUiState = it.moodPickerUiState.copy(isLoadingMovies = false)) }
            return
        }
        val moviesUiStates = movies.toMoodPickerItemsUiState()
        val selectedMovie = moviesUiStates[(0..moviesUiStates.size - 1).random()]
        updateState {
            it.copy(
                moodPickerUiState = it.moodPickerUiState
                    .copy(
                        isLoadingMovies = false,
                        movies = moviesUiStates,
                        selectedMovie = selectedMovie,
                        openMovieDialog = true
                    )
            )
        }
    }


    private fun onGetMoviesByMoodError(exception: AflamiException) {
        updateState {
            it.copy(
                moodPickerUiState = it.moodPickerUiState.copy(
                    error = HomeError.toHomeErrorUiState(exception),
                    isLoadingMovies = false,
                    openMovieDialog = false,
                    selectedMood = null
                )
            )
        }

        sendNewEffect(HomeEffect.ShowGetMoviesByMoodErrorSnackBar)
    }

    override fun onDismissMoodPickerDialog() {
        updateState { it.copy(moodPickerUiState = it.moodPickerUiState.copy(openMovieDialog = false)) }
    }

    override fun onClickViewDetails() {
        onDismissMoodPickerDialog()
        sendNewNavigationEffect(HomeEffect.NavigateToMovieDetailsEffect(movieId = state.value.moodPickerUiState.selectedMovie.id))
    }

    override fun onClickGetAnotherMovie() {
        val currentMovieIndex = state.value.moodPickerUiState.movies.indexOf(
            state.value.moodPickerUiState.selectedMovie
        )
        val newMoviesList = state.value.moodPickerUiState.movies.toMutableList()
        if (currentMovieIndex != -1) {
            newMoviesList.removeAt(currentMovieIndex)
        }
        val nextMovie: MoodPickerItemUiState
        if (newMoviesList.isNotEmpty()) {
            nextMovie = newMoviesList[(0..newMoviesList.size - 1).random()]
            updateState {
                it.copy(
                    moodPickerUiState = it.moodPickerUiState.copy(
                        movies = newMoviesList,
                        selectedMovie = nextMovie
                    )
                )
            }
        } else {
            onClickGetNow()
        }
    }

    override fun onClickUpcomingMovieCard(id: Long) {
        sendNewNavigationEffect(HomeEffect.NavigateToMovieDetailsEffect(movieId = id))
    }

    private fun onError(exception: AflamiException) {
        updateState {
            it.copy(
                error = HomeError.NetworkError,
            )
        }
    }

    private fun onCompletion() = updateState { it.copy(isLoading = false) }
}