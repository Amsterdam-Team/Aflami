package com.amsterdam.viewmodel.continueWatching

import android.annotation.SuppressLint
import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import javax.inject.Inject

class ContinueWatchingUiStateMapper @Inject constructor(){
    @SuppressLint("DefaultLocale")
    fun movieToMediaItemUiState(movie: Movie): MediaItemUiState {
        return MediaItemUiState(
            id = movie.id,
            name = movie.name,
            rate = if (movie.rating % 1 == 0.0f) "${movie.rating.toInt()}" else "%.1f".format(movie.rating),
            posterImageUrl = movie.posterUrl,
            yearOfRelease = movie.releaseDate.year.toString(),
            mediaType = MediaType.MOVIE
        )
    }

    @SuppressLint("DefaultLocale")
    fun tvShowToMediaItemUiState(tvShow: TvShow): MediaItemUiState {
        return MediaItemUiState(
            id = tvShow.id,
            name = tvShow.name,
            rate = if (tvShow.rating % 1 == 0.0f) "${tvShow.rating.toInt()}" else "%.1f".format(tvShow.rating),
            posterImageUrl = tvShow.posterUrl,
            yearOfRelease = tvShow.airDate.year.toString(),
            mediaType = MediaType.TV_SHOW
        )
    }
}