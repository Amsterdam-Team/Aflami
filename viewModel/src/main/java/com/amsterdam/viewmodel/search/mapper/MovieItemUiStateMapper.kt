package com.amsterdam.viewmodel.search.mapper

import com.amsterdam.entity.Movie
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState

fun Movie.toMediaItemUiState(): MovieItemUiState =
    MovieItemUiState(
        id = id,
        name = name,
        posterImageUrl = posterUrl,
        yearOfRelease = releaseDate.year.toString(),
        rate = if (rating % 1 == 0.0f) "${rating.toInt()}" else "%.1f".format(rating)
    )

fun List<Movie>.toMoveUiStates() = map(Movie::toMediaItemUiState)
