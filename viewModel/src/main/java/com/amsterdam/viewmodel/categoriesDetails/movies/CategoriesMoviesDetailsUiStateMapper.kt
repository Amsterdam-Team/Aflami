package com.amsterdam.viewmodel.categoriesDetails.movies

import com.amsterdam.entity.Movie
import com.amsterdam.viewmodel.shared.mappers.toFormattedRating
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import com.amsterdam.viewmodel.utils.toYearString

fun Movie.toMovieUiState(): CategoriesMoviesDetailsUiState.MoviesUiState {
    return CategoriesMoviesDetailsUiState.MoviesUiState(
        id = id,
        name = name,
        posterImageUrl = posterUrl,
        rate = rating.toFormattedRating(),
        yearOfRelease = releaseDate.toYearString(),
        mediaType = MediaType.MOVIE,
        isAdult = isAdult
    )
}