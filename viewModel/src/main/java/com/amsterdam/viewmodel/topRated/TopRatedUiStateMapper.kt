package com.amsterdam.viewmodel.topRated

import android.annotation.SuppressLint
import androidx.paging.PagingData
import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import com.amsterdam.viewmodel.shared.mappers.toFormattedRating
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import com.amsterdam.viewmodel.topRated.TopRatedUiState.TopRatedMediaItemUiState
import com.amsterdam.viewmodel.utils.getMixedItemsList
import kotlinx.coroutines.flow.Flow

fun Flow<PagingData<TopRatedMediaItemUiState>>.toTopRatedUiState(): TopRatedUiState {
    return TopRatedUiState(mediaItems = this)
}

fun getTopRatedMediaItems(
    topRatedMovies: List<Movie>,
    topRatedTvShows: List<TvShow>
): List<TopRatedMediaItemUiState> {
    return getMixedItemsList(
        topRatedMovies,
        topRatedTvShows,
        Movie::toTopRatedMediaItemUiState,
        TvShow::toTopRatedMediaItemUiState
    )
}

@SuppressLint("DefaultLocale")
private fun Movie.toTopRatedMediaItemUiState(): TopRatedMediaItemUiState {
    return TopRatedMediaItemUiState(
        id = id,
        name = name,
        rate = rating.toFormattedRating(),
        posterImageUrl = posterUrl,
        yearOfRelease = releaseDate?.year?.toString() ?: "",
        mediaType = MediaType.MOVIE,
        isAdult = isAdult
    )
}

@SuppressLint("DefaultLocale")
private fun TvShow.toTopRatedMediaItemUiState(): TopRatedMediaItemUiState {
    return TopRatedMediaItemUiState(
        id = id,
        name = name,
        rate = rating.toFormattedRating(),
        posterImageUrl = posterUrl,
        yearOfRelease = airDate?.year?.toString() ?: "",
        mediaType = MediaType.TV_SHOW,
        isAdult = isAdult
    )
}
