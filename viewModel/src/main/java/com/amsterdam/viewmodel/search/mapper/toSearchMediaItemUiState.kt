package com.amsterdam.viewmodel.search.mapper

import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import com.amsterdam.viewmodel.search.uiState.SearchMediaItemUiState
import com.amsterdam.viewmodel.shared.mappers.toFormattedRating
import com.amsterdam.viewmodel.shared.uiStates.MediaType

fun Movie.toSearchMediaItemUiState(): SearchMediaItemUiState {
    return SearchMediaItemUiState(
        id = id,
        name = name,
        posterImageUrl = posterUrl,
        yearOfRelease = releaseDate.year.toString(),
        rate = rating.toFormattedRating(),
        mediaType = MediaType.MOVIE
    )
}

fun TvShow.toSearchMediaItemUiState(): SearchMediaItemUiState {
    return SearchMediaItemUiState(
        id = id,
        name = name,
        posterImageUrl = posterUrl,
        yearOfRelease = airDate.year.toString(),
        rate = rating.toFormattedRating(),
        mediaType = MediaType.TV_SHOW
    )
}

