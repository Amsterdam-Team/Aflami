package com.amsterdam.viewmodel.seriesDetails

import com.amsterdam.viewmodel.shared.BaseViewModel

sealed interface SeriesDetailsEffect : BaseViewModel.BaseUiEffect {
    object NavigateBack : SeriesDetailsEffect
    object NavigateToCastScreen : SeriesDetailsEffect
    object NavigateToLoginScreenEffect : SeriesDetailsEffect
    data class NavigateToSeriesDetails(val tvShowId: Long) : SeriesDetailsEffect
}