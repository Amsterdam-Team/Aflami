package com.amsterdam.viewmodel.continueWatching

import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.entity.TvShowWatchHistory
import com.amsterdam.viewmodel.home.HomeUiState.ContinueWatchingMediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import javax.inject.Inject

class ContinueWatchingUiStateMapper @Inject constructor() {
    fun continueWatchingMediaItemUiState(movieWatchHistory: MovieWatchHistory): ContinueWatchingMediaItemUiState {
        with(movieWatchHistory.movie) {
            return ContinueWatchingMediaItemUiState(
                id = id,
                name = name,
                rate = if (rating % 1.0 == 0.0) rating.toInt().toString() else String.format("%.1f", rating),
                posterImageUrl = posterUrl,
                yearOfRelease = releaseDate.year.toString(),
                dateAdded = movieWatchHistory.lastWatchedTime,
                mediaType = MediaType.MOVIE
            )
        }
    }

    fun continueWatchingMediaItemUiState(tvShowWatchHistory: TvShowWatchHistory): ContinueWatchingMediaItemUiState {
        with(tvShowWatchHistory.tvShow) {
            return ContinueWatchingMediaItemUiState(
                id = id,
                name = name,
                rate = if (rating % 1.0 == 0.0) rating.toInt().toString() else String.format("%.1f", rating),
                posterImageUrl = posterUrl,
                yearOfRelease = airDate.year.toString(),
                dateAdded = tvShowWatchHistory.lastWatchedTime,
                mediaType = MediaType.TV_SHOW
            )
        }
    }
}