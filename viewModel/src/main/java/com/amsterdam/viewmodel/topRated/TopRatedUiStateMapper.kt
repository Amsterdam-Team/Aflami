package com.amsterdam.viewmodel.topRated

import android.annotation.SuppressLint
import androidx.paging.PagingData
import com.amsterdam.domain.useCase.home.GetTopRatedScreenDataUseCase.TopRatedScreenData
import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import com.amsterdam.viewmodel.utils.getMixedItemsList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TopRatedUiStateMapper @Inject constructor() {
    @SuppressLint("DefaultLocale")
    fun toUiState(mediaPagingFlow: Flow<PagingData<MediaItemUiState>>): TopRatedUiState {
        return TopRatedUiState(
           mediaItems =mediaPagingFlow
        )
    }

    fun getTopRatedMediaItems(
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
            rate = if (movie.rating % 1 == 0.0f) "${movie.rating.toInt()}" else "%.1f".format(movie.rating),
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