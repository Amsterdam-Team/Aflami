package com.amsterdam.viewmodel.watchHistory

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.continueWatching.GetContinueWatchingMoviesUseCase
import com.amsterdam.domain.useCase.continueWatching.GetContinueWatchingTvShowsUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.paging.PagingSource
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.TabOption
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import com.amsterdam.viewmodel.watchHistory.WatchHistoryUiState.WatchHistoryMovieUiState
import com.amsterdam.viewmodel.watchHistory.WatchHistoryUiState.WatchHistoryTvShowUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class WatchHistoryViewModel @Inject constructor(
    private val getContinueWatchingMoviesUseCase: GetContinueWatchingMoviesUseCase,
    private val getContinueWatchingTvShowsUseCase: GetContinueWatchingTvShowsUseCase,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<WatchHistoryUiState, WatchHistoryEffect>(
    WatchHistoryUiState(),
    dispatcherProvider
),
    WatchHistoryInteractionListener {

    init {
        manageLocaleLanguageUseCase.getAppLanguage()
            .onEach {
                getMoviesWatchHistoryData()
            }.launchIn(viewModelScope)
        getMoviesWatchHistoryData()
    }

    private fun getMoviesWatchHistoryData() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = {
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            getContinueWatchingMoviesUseCase(page = page, pageSize = 20).first()
                        }
                    }
                ).flow.map { it.map { it.movie.toWatchHistoryItemUiState() } }
                    .cachedIn(viewModelScope)
            },
            onSuccess = ::onGetMoviesContinueWatchingDataSuccess,
            onError = ::onError,
            onCompletion = ::onCompletion
        )
    }

    private fun getTvShowsWatchHistoryData() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = {
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            getContinueWatchingTvShowsUseCase(page = page, pageSize = 20).first()
                        }
                    }
                ).flow.map { it.map { it.tvShow.toWatchHistoryItemUiState() } }
                    .cachedIn(viewModelScope)
            },
            onSuccess = ::onGetTvShowContinueWatchingDataSuccess,
            onError = ::onError,
            onCompletion = ::onCompletion
        )
    }

    fun onGetMoviesContinueWatchingDataSuccess(
        movieItems: Flow<PagingData<WatchHistoryMovieUiState>>
    ) {
        updateState { currentState ->
            currentState.copy(
                movies = movieItems
            )
        }
    }

    fun onGetTvShowContinueWatchingDataSuccess(
        tvShowItems: Flow<PagingData<WatchHistoryTvShowUiState>>
    ) {
        updateState { currentState ->
            currentState.copy(
                tvShows = tvShowItems
            )
        }
    }

    private fun onError(exception: AflamiException) {
        when (exception) {
            is NoInternetException -> updateState {
                it.copy(
                    error = WatchHistoryUiState.WatchHistoryError.NetworkError
                )
            }

            else -> {}
        }
    }

    override fun onClickMediaItem(mediaId: Long, mediaType: MediaType) {
        if (mediaType == MediaType.MOVIE)
            sendNewEffect(WatchHistoryEffect.NavigateToMovieDetails(mediaId))
        else
            sendNewEffect(WatchHistoryEffect.NavigateToTvShowDetails(mediaId))
    }

    override fun onClickTabOption(option: TabOption) {
        updateState { it.copy(selectedTabOption = option) }
        if (option == TabOption.MOVIES) {
            getMoviesWatchHistoryData()
        } else {
            getTvShowsWatchHistoryData()
        }
    }

    override fun onClickRetryLoading() {
        getMoviesWatchHistoryData()
    }

    override fun onClickMovieCard(movieId: Long) {
        sendNewNavigationEffect(WatchHistoryEffect.NavigateToMovieDetails(movieId))
    }

    override fun onClickTvShowCard(tvShowId: Long) {
        sendNewNavigationEffect(WatchHistoryEffect.NavigateToTvShowDetails(tvShowId))
    }

    override fun onClickBack() {
        sendNewEffect(WatchHistoryEffect.NavigateBack)
    }

    private fun onCompletion() = updateState { it.copy(isLoading = false) }
}