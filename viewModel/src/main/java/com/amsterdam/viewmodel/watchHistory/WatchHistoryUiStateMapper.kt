package com.amsterdam.viewmodel.watchHistory

import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import com.amsterdam.viewmodel.shared.mappers.toFormattedRating
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import com.amsterdam.viewmodel.watchHistory.WatchHistoryUiState.WatchHistoryItemUiState

fun Movie.toWatchHistoryItemUiState(): WatchHistoryItemUiState {
    return WatchHistoryItemUiState(
        id = id,
        name = name,
        posterImageUrl = posterUrl,
        yearOfRelease = releaseDate.year.toString(),
        rate = rating.toFormattedRating(),
        mediaType = MediaType.MOVIE
    )
}
fun TvShow.toWatchHistoryItemUiState(): WatchHistoryItemUiState {
    return WatchHistoryItemUiState(
        id = id,
        name = name,
        posterImageUrl = posterUrl,
        yearOfRelease = airDate.year.toString(),
        rate = rating.toFormattedRating(),
        mediaType = MediaType.TV_SHOW
    )
}

