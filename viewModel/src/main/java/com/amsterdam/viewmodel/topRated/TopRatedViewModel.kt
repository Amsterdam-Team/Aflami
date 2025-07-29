package com.amsterdam.viewmodel.topRated

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.home.GetTopRatedScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetTopRatedTvShowsUseCase
import com.amsterdam.paging.PagingSource
import com.amsterdam.viewmodel.search.mapper.toMediaItemUiState
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import com.amsterdam.viewmodel.topRated.TopRatedUiState.TopRatedError
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class TopRatedViewModel @Inject constructor(
    private val getTopRatedScreenDataUseCase: GetTopRatedScreenDataUseCase,
    private val topRatedUiStateMapper: TopRatedUiStateMapper,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<TopRatedUiState, TopRatedEffect>(TopRatedUiState(), dispatcherProvider),
    TopRatedInteractionListener {

    init {
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
                     getTopRatedScreenDataUseCase(page).topRatedMovies

                        }
                    }
                )
                    .flow.map { pagingData ->
                        pagingData.map {
                            it.toMediaItemUiState()
                        }
                    }
                    .cachedIn(viewModelScope)
            },
            onSuccess = ::onGetTopRatedMoviesSuccess,
            onError = ::onError
        )
    }



    private fun onGetTopRatedMoviesSuccess(moviesPagingFlow: Flow<PagingData<MovieItemUiState>>) {
        updateState { topRatedUiStateMapper.toUiState(moviesPagingFlow) }
    }

    private fun onError(exception: AflamiException) {
        when (exception) {
            is NoInternetException -> updateState {
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
            sendNewEffect(TopRatedEffect.NavigateToMovieDetailsScreen(mediaId))
        else
            sendNewEffect(TopRatedEffect.NavigateToTvShowDetailsEffect(mediaId))
    }

    override fun onClickRetryLoading() {
        getTopRatedScreenData()
    }

    override fun onClickBack() {
        sendNewEffect(TopRatedEffect.NavigateBack)
    }
}