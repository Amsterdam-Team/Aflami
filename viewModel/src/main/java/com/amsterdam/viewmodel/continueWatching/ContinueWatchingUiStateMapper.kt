package com.amsterdam.viewmodel.continueWatching

import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.entity.TvShowWatchHistory
import com.amsterdam.viewmodel.continueWatching.ContinueWatchingUiState.ContinueWatchingItemUiState
import com.amsterdam.viewmodel.shared.mappers.toFormattedRating
import com.amsterdam.viewmodel.shared.uiStates.MediaType

fun MovieWatchHistory.toContinueWatchingItemUiState(): ContinueWatchingItemUiState {
    with(movie) {
        return ContinueWatchingItemUiState(
            id = id,
            name = name,
            rate = rating.toFormattedRating(),
            posterImageUrl = posterUrl,
            yearOfRelease = releaseDate?.year?.toString() ?: "",
            dateAdded = lastWatchedTime,
            mediaType = MediaType.MOVIE,
            isAdult = isAdult
        )
    }
}

fun TvShowWatchHistory.toContinueWatchingItemUiState(): ContinueWatchingItemUiState {
    with(tvShow) {
        return ContinueWatchingItemUiState(
            id = id,
            name = name,
            rate = rating.toFormattedRating(),
            posterImageUrl = posterUrl,
            yearOfRelease = airDate?.year?.toString() ?: "",
            dateAdded = lastWatchedTime,
            mediaType = MediaType.TV_SHOW,
            isAdult = isAdult
        )
    }
}