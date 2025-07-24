package com.example.viewmodel.topRated

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NoInternetException
import com.example.domain.useCase.GetTopRatedScreenDataUseCase
import com.example.domain.useCase.GetTopRatedScreenDataUseCase.TopRatedScreenData
import com.example.viewmodel.shared.BaseViewModel
import com.example.viewmodel.topRated.TopRatedUiState.TopRatedError
import com.example.viewmodel.utils.dispatcher.DispatcherProvider

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

    override fun onClickMovie(movieId: Long) {
        sendNewEffect(TopRatedEffect.NavigateToMovieDetailsScreen(movieId))
    }

    override fun onClickRetryLoading() {
        getTopRatedScreenData()
    }

    override fun onClickBack() {
        sendNewEffect(TopRatedEffect.NavigateBack)
    }
}