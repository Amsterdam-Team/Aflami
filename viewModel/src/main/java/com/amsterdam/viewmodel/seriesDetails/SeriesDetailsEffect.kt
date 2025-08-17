package com.amsterdam.viewmodel.seriesDetails

import com.amsterdam.viewmodel.shared.BaseViewModel

sealed interface SeriesDetailsEffect : BaseViewModel.BaseUiEffect {
    data object NavigateBack : SeriesDetailsEffect
    data object NavigateToCastScreen : SeriesDetailsEffect
    data object NavigateToLoginScreenEffect : SeriesDetailsEffect
    data class NavigateToSeriesDetails(val tvShowId: Long) : SeriesDetailsEffect
    data class LaunchSeriesVideoEffect(val url: String) : SeriesDetailsEffect

    data object ShowRatingSuccessSnackBar: SeriesDetailsEffect
    data object ShowRatingErrorSnackBar: SeriesDetailsEffect

    data object ShowEpisodeTrailerNotFound: SeriesDetailsEffect
}