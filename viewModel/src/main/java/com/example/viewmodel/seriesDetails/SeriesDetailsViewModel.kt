package com.example.viewmodel.seriesDetails

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NoInternetException
import com.example.domain.useCase.GetEpisodesBySeasonNumberUseCase
import com.example.domain.useCase.GetTvShowDetailsUseCase
import com.example.domain.useCase.GetTvShowDetailsUseCase.TvShowDetails
import com.example.entity.Episode
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.seriesDetails.SeriesDetailsUiState.SeriesExtras
import com.example.viewmodel.utils.dispatcher.DispatcherProvider

class SeriesDetailsViewModel(
    args: SeriesDetailsArgs,
    private val seriesDetailsStateMapper: SeriesDetailsStateMapper,
    private val getTvShowDetailsUseCase: GetTvShowDetailsUseCase,
    private val getEpisodesBySeasonNumberUseCase: GetEpisodesBySeasonNumberUseCase,
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

    private suspend fun getTvShowDetails() = getTvShowDetailsUseCase(state.value.tvShowId)

    private fun onGetTvShowDetailsSuccess(tvShowDetails: TvShowDetails) {
        updateState {
            seriesDetailsStateMapper.mapToSeriesDetailsUiState(tvShowDetails)
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

    override fun onClickSeasonMenu(seasonNumber: Int) {
        tryToExecute(
            action = { getEpisodesForSeason(seasonNumber) },
            onSuccess = { episodes -> onGetEpisodesSuccess(seasonNumber, episodes) },
            onError = ::onError,
        )
    }

    private suspend fun getEpisodesForSeason(seasonNumber: Int) =
        getEpisodesBySeasonNumberUseCase(state.value.tvShowId, seasonNumber)

    private fun onGetEpisodesSuccess(seasonNumber: Int, episodes: List<Episode>) {
        val updatedSeasons = state.value.seasons.map {
            if (it.seasonNumber == seasonNumber) {
                it.copy(episodes = seriesDetailsStateMapper.mapToEpisodeUiState(episodes))
            } else {
                it
            }
        }
        updateState { it.copy(seasons = updatedSeasons) }
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