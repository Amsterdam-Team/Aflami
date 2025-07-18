package com.example.viewmodel.seriesDetails

import com.example.domain.useCase.GetEpisodesBySeasonNumber
import com.example.domain.useCase.GetTvShowDetailsUseCase
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.seriesDetails.SeriesDetailsUiState.SeriesExtras
import com.example.viewmodel.utils.dispatcher.DispatcherProvider

class SeriesDetailsViewModel(
    args: SeriesDetailsArgs,
    private val seriesDetailsStateMapper: SeriesDetailsStateMapper,
    private val getTvShowDetailsUseCase: GetTvShowDetailsUseCase,
    private val getEpisodesBySeasonNumber: GetEpisodesBySeasonNumber,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<SeriesDetailsUiState, SeriesDetailsEffect>(
    SeriesDetailsUiState(),
    dispatcherProvider
), SeriesDetailsInteractionListener {
    override fun onClickSeriesExtraItem(seriesExtras: SeriesExtras) {
        TODO("Not yet implemented")
    }

    override fun onNavigateBack() {
        TODO("Not yet implemented")
    }

    override fun onClickRetryButton() {
        TODO("Not yet implemented")
    }

    override fun onClickShowAllCast() {
        TODO("Not yet implemented")
    }

    override fun onClickSeasonMenu(seasonNumber: Int) {
        TODO("Not yet implemented")
    }


}