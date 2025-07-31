package com.amsterdam.viewmodel.topRated

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.useCase.home.GetTopRatedScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetTopRatedScreenDataUseCase.TopRatedScreenData
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import com.amsterdam.viewmodel.topRated.TopRatedUiState.TopRatedError
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
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
        manageLocaleLanguageUseCase.getDeviceLanguage()
            .onEach {
                getTopRatedScreenData()
            }.launchIn(viewModelScope)

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

    override fun onClickBack() {
        sendNewNavigationEffect(TopRatedEffect.NavigateBack)
    }
}