package com.amsterdam.viewmodel.continueWatching

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.home.GetContinueWatchingScreenDataUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.entity.TvShowWatchHistory
import com.amsterdam.paging.PagingSource
import com.amsterdam.viewmodel.continueWatching.ContinueWatchingUiState.ContinueWatchingError
import com.amsterdam.viewmodel.continueWatching.ContinueWatchingUiState.ContinueWatchingItemUiState
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import com.amsterdam.viewmodel.utils.getLinearItemsList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ContinueWatchingViewModel @Inject constructor(
    private val getContinueWatchingScreenDataUseCase: GetContinueWatchingScreenDataUseCase,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<ContinueWatchingUiState, ContinueWatchingEffect>(
    ContinueWatchingUiState(),
    dispatcherProvider
),
    ContinueWatchingInteractionListener {

    init {
        manageLocaleLanguageUseCase.getAppLanguage()
            .onEach {
                getContinueWatchingData()
            }.launchIn(viewModelScope)
    }

    private fun getContinueWatchingData() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = {
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            val continueWatchingScreenData = getContinueWatchingScreenDataUseCase(
                                page = page,
                                pageSize = 20
                            ).first()

                            val mergedItems = getLinearItemsList(
                                continueWatchingScreenData.continueWatchingMovies,
                                continueWatchingScreenData.continueWatchingTvShows,
                                MovieWatchHistory::toContinueWatchingItemUiState,
                                TvShowWatchHistory::toContinueWatchingItemUiState
                            )
                                .sortedByDescending { it.dateAdded }
                                .take(10)
                            mergedItems
                        }
                    }
                ).flow.cachedIn(viewModelScope)
            },
            onSuccess = ::onGetContinueWatchingScreenDataSuccess,
            onError = ::onError,
            onCompletion = ::onCompletion
        )
    }

    fun onGetContinueWatchingScreenDataSuccess(mediaItems: Flow<PagingData<ContinueWatchingItemUiState>>) {
        updateState { currentState ->
            currentState.copy(
                continueMediaItemUiStates = mediaItems
            )
        }
    }

    private fun onError(exception: AflamiException) {
        when (exception) {
            is NoInternetException -> updateState {
                it.copy(
                    error = ContinueWatchingError.NetworkError
                )
            }

            else -> {}
        }
    }

    override fun onClickMediaItem(mediaId: Long, mediaType: MediaType) {
        if (mediaType == MediaType.MOVIE)
            sendNewNavigationEffect(ContinueWatchingEffect.NavigateToMovieDetailsScreen(mediaId))
        else
            sendNewNavigationEffect(ContinueWatchingEffect.NavigateToTvShowDetailsEffect(mediaId))
    }

    override fun onClickRetryLoading() {
        getContinueWatchingData()
    }

    override fun onClickBack() {
        sendNewNavigationEffect(ContinueWatchingEffect.NavigateBack)
    }

    private fun onCompletion() = updateState { it.copy(isLoading = false) }
}