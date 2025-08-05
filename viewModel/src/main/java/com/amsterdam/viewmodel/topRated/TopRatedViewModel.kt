package com.amsterdam.viewmodel.topRated

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.useCase.home.GetTopRatedScreenDataUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.paging.PagingSource
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import com.amsterdam.viewmodel.topRated.TopRatedUiState.TopRatedError
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TopRatedViewModel @Inject constructor(
    private val getTopRatedScreenDataUseCase: GetTopRatedScreenDataUseCase,
    private val topRatedUiStateMapper: TopRatedUiStateMapper,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<TopRatedUiState, TopRatedEffect>(TopRatedUiState(), dispatcherProvider),
    TopRatedInteractionListener {

    init {
        manageLocaleLanguageUseCase.getAppLanguage()
            .onEach {
                getTopRatedScreenData()
            }.launchIn(viewModelScope)

        getTopRatedScreenData()
    }

    private fun getTopRatedScreenData() {
        updateState { it.copy(isLoading = true) }

        tryToExecute(
            action = {
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            val result = getTopRatedScreenDataUseCase(page)
                            topRatedUiStateMapper.getTopRatedMediaItems(
                                result.topRatedMovies,
                                result.topRatedTvShows
                            )

                        }
                    }
                ).flow
                    .cachedIn(viewModelScope)
            },
            onSuccess = ::onGetTopRatedMoviesSuccess,
            onError = {}
        )
    }


    private fun onGetTopRatedMoviesSuccess(mediaPagingFlow: Flow<PagingData<MediaItemUiState>>) {
        updateState { topRatedUiStateMapper.toUiState(mediaPagingFlow) }
    }

    private fun onError(exception: AflamiException) {
        when (exception) {
            is NetworkException -> updateState {
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

    override fun onClickMediaItem(mediaId: Long, mediaType: MediaType) {
        if (mediaType == MediaType.MOVIE)
            sendNewNavigationEffect(TopRatedEffect.NavigateToMovieDetailsScreen(mediaId))
        else
            sendNewNavigationEffect(TopRatedEffect.NavigateToTvShowDetailsEffect(mediaId))
    }

    override fun onClickRetryLoading() {
        getTopRatedScreenData()
    }

    override fun onPagingLoadStateChanged(loadStates: CombinedLoadStates) {
        when (val refreshState = loadStates.refresh
        ) {
            is LoadState.Loading -> {
                updateState { it.copy(isLoading = true, error = null) }
            }

            is LoadState.NotLoading -> {
                updateState { it.copy(isLoading = false) }
            }

            is LoadState.Error -> {
                updateState { it.copy(isLoading = false) }
                onError(refreshState.error as AflamiException)
            }
        }
    }

    override fun onClickBack() {
        sendNewNavigationEffect(TopRatedEffect.NavigateBack)
    }
}