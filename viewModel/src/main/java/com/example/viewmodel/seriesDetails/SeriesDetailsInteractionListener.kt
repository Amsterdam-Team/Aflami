package com.example.viewmodel.seriesDetails

import com.example.viewmodel.seriesDetails.SeriesDetailsUiState.SeriesExtras

interface SeriesDetailsInteractionListener {
    fun onClickSeriesExtraItem(seriesExtras: SeriesExtras)
    fun onNavigateBack()
    fun onClickRetryButton()
    fun onClickShowAllCast()
    fun onClickSeasonMenu(seasonNumber: Int)
}