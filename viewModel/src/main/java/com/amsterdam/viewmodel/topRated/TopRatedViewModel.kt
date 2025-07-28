package com.amsterdam.viewmodel.topRated

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.home.GetTopRatedScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetTopRatedScreenDataUseCase.TopRatedScreenData
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import com.amsterdam.viewmodel.topRated.TopRatedUiState.TopRatedError
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider

class TopRatedViewModel(
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
            action = { getTopRatedScreenDataUseCase() },
            onSuccess = ::onGetTopRatedMoviesSuccess,
            onError = ::onError
        )
    }

    private fun onGetTopRatedMoviesSuccess(topRatedScreenData: TopRatedScreenData) {
        updateState { topRatedUiStateMapper.toUiState(topRatedScreenData) }
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