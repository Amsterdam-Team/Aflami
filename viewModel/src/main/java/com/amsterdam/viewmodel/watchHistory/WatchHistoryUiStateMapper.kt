package com.amsterdam.viewmodel.watchHistory

import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import com.amsterdam.viewmodel.shared.mappers.toFormattedRating
import com.amsterdam.viewmodel.watchHistory.WatchHistoryUiState.WatchHistoryMovieUiState
import com.amsterdam.viewmodel.watchHistory.WatchHistoryUiState.WatchHistoryTvShowUiState

fun Movie.toWatchHistoryItemUiState(): WatchHistoryMovieUiState {
    return WatchHistoryMovieUiState(
        id = id,
        name = name,
        posterImageUrl = posterUrl,
        yearOfRelease = releaseDate?.year?.toString() ?: "",
        rate = rating.toFormattedRating(),
    )
}

fun TvShow.toWatchHistoryItemUiState(): WatchHistoryTvShowUiState {
    return WatchHistoryTvShowUiState(
        id = id,
        name = name,
        posterImageUrl = posterUrl,
        yearOfRelease = airDate?.year.toString(),
        rate = rating.toFormattedRating(),
    )
}

