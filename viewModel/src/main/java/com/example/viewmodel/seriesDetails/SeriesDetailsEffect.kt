package com.example.viewmodel.seriesDetails

import com.example.viewmodel.shared.BaseViewModel

sealed interface SeriesDetailsEffect : BaseViewModel.BaseUiEffect {
    object NavigateBack : SeriesDetailsEffect
    object NavigateToCastScreen : SeriesDetailsEffect
}