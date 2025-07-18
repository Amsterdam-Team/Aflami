package com.example.viewmodel.seriesDetails

import com.example.viewmodel.BaseViewModel

sealed interface SeriesDetailsEffect: BaseViewModel.BaseUiEffect {
    object NavigateBack : SeriesDetailsEffect
    object NavigateToCastScreen : SeriesDetailsEffect
}