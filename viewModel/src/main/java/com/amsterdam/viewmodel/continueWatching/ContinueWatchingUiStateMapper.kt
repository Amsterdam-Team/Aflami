package com.amsterdam.viewmodel.continueWatching

import android.annotation.SuppressLint
import com.amsterdam.domain.useCase.home.GetContinueWatchingScreenDataUseCase.ContinueWatchingScreenData
import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import com.amsterdam.viewmodel.utils.getLinearItemsList


class ContinueWatchingUiStateMapper {
    @SuppressLint("DefaultLocale")
    fun toUiState(
        continueWatchingScreenData: ContinueWatchingScreenData
    ): ContinueWatchingUiState {
        return ContinueWatchingUiState(
            continueMediaItemUiStates = getContinueWatchingMediaItems(
                continueWatchingScreenData.continueWatchingMovies,
                continueWatchingScreenData.continueWatchingTvShows
            )
        )
    }

    private fun getContinueWatchingMediaItems(
        movies: List<Movie>,
        tvShows: List<TvShow>
    ): List<MediaItemUiState> {
        return getLinearItemsList(
            movies,
            tvShows,
            ::movieToMediaItemUiState,
            ::tvShowToMediaItemUiState
        )
    }

    @SuppressLint("DefaultLocale")
    fun movieToMediaItemUiState(movie: Movie): MediaItemUiState {
        return MediaItemUiState(
            id = movie.id,
            name = movie.name,
            rate = String.format("%.1f", movie.rating),
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
            rate = String.format("%.1f", tvShow.rating),
            posterImageUrl = tvShow.posterUrl,
            yearOfRelease = tvShow.airDate.year.toString(),
            mediaType = MediaType.TV_SHOW
        )
    }
}