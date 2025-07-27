package com.amsterdam.viewmodel.continueWatching

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.home.GetContinueWatchingMoviesUseCase
import com.amsterdam.domain.useCase.home.GetContinueWatchingScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetContinueWatchingScreenDataUseCase.ContinueWatchingScreenData
import com.amsterdam.domain.useCase.home.GetContinueWatchingTvShowsUseCase
import com.amsterdam.viewmodel.home.HomeEffect
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider

class ContinueWatchingViewModel(
    private val getContinueWatchingMoviesUseCase: GetContinueWatchingMoviesUseCase,
    private val getContinueWatchingTvShowsUseCase: GetContinueWatchingTvShowsUseCase,
    private val getContinueWatchingScreenDataUseCase: GetContinueWatchingScreenDataUseCase,
    private val continueWatchingUiStateMapper: ContinueWatchingUiStateMapper,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<ContinueWatchingUiState, ContinueWatchingEffect>(
    ContinueWatchingUiState(),
    dispatcherProvider
),
    ContinueWatchingInteractionListener {

    init {
        getContinueWatchingData()
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
        updateState { continueWatchingUiStateMapper.toUiState(continueWatchingData) }
    }

   /* private fun getContinueWatchingMovies() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = ::loadContinueWatchingMovies,
            onSuccess = ::onGetContinueWatchingMoviesSuccess,
            onError = ::onError,
            onCompletion = ::onCompletion
        )
    }

    private suspend fun loadContinueWatchingMovies(): List<Movie> {
        return getContinueWatchingMoviesUseCase()
            .firstOrNull() ?: emptyList()
    }

    private fun onGetContinueWatchingMoviesSuccess(continueWatchingMovies: List<Movie>) {
        updateState { continueWatchingUiStateMapper.toUiState() }
    }
*/
    private fun onError(exception: AflamiException) {
        when (exception) {
            is NoInternetException -> updateState {
                it.copy(
                    error = ContinueWatchingUiState.ContinueWatchingError.NetworkError
                )
            }
            else ->{}
        }
    }

    override fun onClickMediaItem(mediaId: Long, mediaType: MediaType) {
        if (mediaType == MediaType.MOVIE)
            sendNewEffect(ContinueWatchingEffect.NavigateToMovieDetailsScreen(mediaId))
        else
            sendNewEffect(ContinueWatchingEffect.NavigateToTvShowDetailsEffect(mediaId))
    }

    override fun onClickRetryLoading() {
        getContinueWatchingData()
    }

    override fun onClickBack() {
        sendNewEffect(ContinueWatchingEffect.NavigateBack)
    }

    private fun onCompletion() = updateState { it.copy(isLoading = false) }
}