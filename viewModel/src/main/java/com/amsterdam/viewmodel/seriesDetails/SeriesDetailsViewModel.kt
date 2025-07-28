package com.amsterdam.viewmodel.seriesDetails

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.details.GetEpisodesBySeasonNumberUseCase
import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase
import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase.TvShowDetails
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.entity.Episode
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeriesExtras
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.MovieAndSeriesDetailsDialogType
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesDetailsViewModel @Inject constructor(
    args: SeriesDetailsArgs,
    private val seriesDetailsStateMapper: SeriesDetailsStateMapper,
    private val getTvShowDetailsUseCase: GetTvShowDetailsUseCase,
    private val getEpisodesBySeasonNumberUseCase: GetEpisodesBySeasonNumberUseCase,
    private val getsSessionType: GetsSessionType,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<SeriesDetailsUiState, SeriesDetailsEffect>(
    SeriesDetailsUiState(),
    dispatcherProvider
), SeriesDetailsInteractionListener {

    init {
        val tvShowId = args.tvShowId!!
        updateState { it.copy(tvShowId = tvShowId) }
        loadTvShowDetails()
    }

    private fun loadTvShowDetails() {
        updateState { it.copy(isLoading = true, networkError = false) }
        tryToExecute(
            action = ::getTvShowDetails,
            onSuccess = ::onGetTvShowDetailsSuccess,
            onError = ::onError,
        )
    }

    private suspend fun getTvShowDetails(): TvShowDetails {
        return getTvShowDetailsUseCase(state.value.tvShowId)
    }
    private fun onGetTvShowDetailsSuccess(tvShowDetails: TvShowDetails) {
        updateState {
            seriesDetailsStateMapper.toUiState(tvShowDetails)
        }
    }

    override fun onClickSeriesExtraItem(seriesExtras: SeriesExtras) {
        updateState { state ->
            state.copy(
                extraItem = state.extraItem.map { selectable ->
                    selectable.copy(isSelected = selectable.item == seriesExtras)
                }
            )
        }
    }

    override fun onNavigateBack() {
        sendNewEffect(SeriesDetailsEffect.NavigateBack)
    }

    override fun onClickRetryButton() {
        loadTvShowDetails()
    }

    override fun onClickShowAllCast() {
        sendNewEffect(SeriesDetailsEffect.NavigateToCastScreen)
    }

    override fun onAddToListClicked() {
        viewModelScope.launch {
            runIfLoggedIn(
                onLoggedIn = {},
                onGuest = { showMustLoginDialog(MovieAndSeriesDetailsDialogType.AddToList) }
            )
        }
    }

    override fun onRateClicked() {
        viewModelScope.launch {
            runIfLoggedIn(
                onLoggedIn = {},
                onGuest = { showMustLoginDialog(MovieAndSeriesDetailsDialogType.Rate) }
            )
        }
    }

    override fun onClickSeasonMenu(seasonNumber: Int) {
        tryToExecute(
            action = { getEpisodesForSeason(seasonNumber) },
            onSuccess = { episodes -> onGetEpisodesSuccess(seasonNumber, episodes) },
            onError = ::onError,
        )
    }

    override fun onNavigateToLoginClicked() {
        sendNewEffect(SeriesDetailsEffect.NavigateToLoginScreenEffect)
    }

    override fun onCancelClicked() {
        updateState { it.copy(isLoginDialogVisible = false) }
    }

    private suspend fun getEpisodesForSeason(seasonNumber: Int): List<Episode> {
        val updatedSeasons = state.value.seasons.map {
            if (it.seasonNumber == seasonNumber && it.episodes.isNotEmpty()) {
                it.copy(isExpanded = !it.isExpanded)
            } else {
                it
            }
        }
        if (updatedSeasons != state.value.seasons) {
            updateState { it.copy(seasons = updatedSeasons) }
            return emptyList()
        }
        return getEpisodesBySeasonNumberUseCase(state.value.tvShowId, seasonNumber)
    }


    private fun onGetEpisodesSuccess(seasonNumber: Int, episodes: List<Episode>) {
        if (episodes.isEmpty()) {
            return
        }
        val updatedSeasons = state.value.seasons.map {
            if (it.seasonNumber == seasonNumber) {
                it.copy(
                    episodes = seriesDetailsStateMapper.mapToEpisodeUiState(episodes),
                    isExpanded = true
                )
            } else {
                it
            }
        }
        updateState {
            it.copy(seasons = updatedSeasons)
        }
    }

    private suspend fun runIfLoggedIn(
        onLoggedIn: () -> Unit,
        onGuest: () -> Unit
    ) {
        if (getsSessionType() == SessionType.LOGGED_IN) {
            onLoggedIn()
        } else {
            onGuest()
        }
    }

    private fun showMustLoginDialog(dialogType: MovieAndSeriesDetailsDialogType){
        updateState { it.copy(isLoginDialogVisible = true, dialogType = dialogType) }
    }

    private fun onError(exception: AflamiException) {
        when (exception) {
            is NoInternetException -> updateState {
                it.copy(
                    isLoading = false,
                    networkError = true
                )
            }
            else -> {}
        }
    }
}