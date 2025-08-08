package com.amsterdam.viewmodel.listDetails

import com.amsterdam.entity.Movie
import com.amsterdam.viewmodel.listDetails.ListDetailsUiState.ListDetailsItemsUiState
import com.amsterdam.viewmodel.shared.mappers.toFormattedRating
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import com.amsterdam.viewmodel.utils.toYearString

fun Movie.toListDetailsItemUiState(): ListDetailsItemsUiState {
    return ListDetailsItemsUiState(
        id = id,
        name = name,
        yearOfRelease = releaseDate.toYearString(),
        posterImageUrl = posterUrl,
        rate = rating.toFormattedRating(),
        mediaType = MediaType.MOVIE
    )
}

fun List<Movie>.toListDetailsItemUiState(): List<ListDetailsItemsUiState> = map { it.toListDetailsItemUiState() }
