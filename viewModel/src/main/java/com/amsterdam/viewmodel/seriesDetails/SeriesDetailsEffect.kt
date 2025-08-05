package com.amsterdam.viewmodel.seriesDetails

import com.amsterdam.viewmodel.shared.BaseViewModel

sealed interface SeriesDetailsEffect : BaseViewModel.BaseUiEffect {
    object NavigateBack : SeriesDetailsEffect
    object NavigateToCastScreen : SeriesDetailsEffect
    object NavigateToLoginScreenEffect : SeriesDetailsEffect
    data class NavigateToSeriesDetails(val tvShowId: Long) : SeriesDetailsEffect
    data class LaunchSeriesVideoEffect(val url: String) : SeriesDetailsEffect

    object ShowRatingSuccessSnackBar: SeriesDetailsEffect
    object ShowRatingErrorSnackBar: SeriesDetailsEffect
}