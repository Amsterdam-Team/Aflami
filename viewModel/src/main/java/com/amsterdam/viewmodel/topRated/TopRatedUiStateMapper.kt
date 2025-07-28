package com.amsterdam.viewmodel.topRated

import android.annotation.SuppressLint
import com.amsterdam.domain.useCase.home.GetTopRatedScreenDataUseCase.TopRatedScreenData
import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import com.amsterdam.viewmodel.utils.getMixedItemsList
import javax.inject.Inject

class TopRatedUiStateMapper @Inject constructor() {
    @SuppressLint("DefaultLocale")
    fun toUiState(topRatedScreenData: TopRatedScreenData): TopRatedUiState {
        return TopRatedUiState(
            topRatedMediaItems = getTopRatedMediaItems(
                topRatedScreenData.topRatedMovies,
                topRatedScreenData.topRatedTvShows
            )
        )
    }

    private fun getTopRatedMediaItems(
        topRatedMovies: List<Movie>,
        topRatedTvShows: List<TvShow>
    ): List<MediaItemUiState> {
        return getMixedItemsList(
            topRatedMovies,
            topRatedTvShows,
            ::movieToTopRatedMediaItemUiState,
            ::tvShowToTopRatedMediaItemUiState
        )
    }

    @SuppressLint("DefaultLocale")
    private fun movieToTopRatedMediaItemUiState(movie: Movie): MediaItemUiState {
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
    private fun tvShowToTopRatedMediaItemUiState(tvShow: TvShow): MediaItemUiState {
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