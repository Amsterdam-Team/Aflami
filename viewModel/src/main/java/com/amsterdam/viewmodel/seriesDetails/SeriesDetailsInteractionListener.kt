package com.amsterdam.viewmodel.seriesDetails

import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeriesExtras

interface SeriesDetailsInteractionListener {
    fun onClickSeriesExtraItem(seriesExtras: SeriesExtras)
    fun onNavigateBack()
    fun onClickRetryButton()
    fun onClickShowAllCast()
    fun onClickRate()
    fun onClickSeasonMenu(seasonNumber: Int)
    fun onNavigateToLoginClicked()
    fun onCancelClicked()
    fun onClickSimilarMovie(movieId: Long)
    fun onDescriptionExpansionToggled()
    fun onReviewExpansionToggled(reviewId: String)
    fun onPlayVideoClicked()
    fun onPlayEpisodeClicked(seasonNumber: Int, episodeNumber: Int)
}