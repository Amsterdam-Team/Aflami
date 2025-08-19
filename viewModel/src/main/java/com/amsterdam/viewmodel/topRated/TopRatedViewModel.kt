package com.amsterdam.viewmodel.topRated

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.topRated.GetTopRatedDataUseCase
import com.amsterdam.paging.PagingSource
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import com.amsterdam.viewmodel.topRated.TopRatedUiState.TopRatedMediaItemUiState
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TopRatedViewModel @Inject constructor(
    private val getTopRatedScreenDataUseCase: GetTopRatedDataUseCase,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<TopRatedUiState, TopRatedEffect>(TopRatedUiState(), dispatcherProvider),
    TopRatedInteractionListener {

    init {
        showLoadingState()
        manageLocaleLanguageUseCase.getAppLanguage()
                .onEach {
                    getTopRatedScreenData()
                }.launchIn(viewModelScope)

        getTopRatedScreenData()
    }

    private fun getTopRatedScreenData() {
        showLoadingState()
        tryToExecute(
            action = {
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            getTopRatedScreenDataUseCase(page).let { result ->
                                getTopRatedMediaItems(
                                    result.topRatedMovies,
                                    result.topRatedTvShows
                                )
                            }
                        }
                    }
                ).flow.cachedIn(viewModelScope)
            },
            onSuccess = ::onGetTopRatedMoviesSuccess,
            onCompletion = ::onGetTopRatedMoviesCompletion
        )
    }


    private fun onGetTopRatedMoviesSuccess(mediaPagingFlow: Flow<PagingData<TopRatedMediaItemUiState>>) {
        updateState { mediaPagingFlow.toTopRatedUiState() }
    }

    private fun onGetTopRatedMoviesCompletion() = updateState { it.copy(isLoading = false) }

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
        when (val refreshState = loadStates.refresh) {
            is LoadState.Loading -> {
                resetErrorStateToNull()
                updateState { it.copy(isLoading = true) }
            }

            is LoadState.NotLoading -> {
                updateState { it.copy(isLoading = false) }
            }

            is LoadState.Error -> {
                updateState { it.copy(isLoading = false) }
                updateErrorStateByException(refreshState.error as AflamiException?)
            }
        }
    }

    override fun onClickBack() {
        sendNewNavigationEffect(TopRatedEffect.NavigateBack)
    }

    private fun showLoadingState() = updateState { it.copy(isLoading = true) }
}