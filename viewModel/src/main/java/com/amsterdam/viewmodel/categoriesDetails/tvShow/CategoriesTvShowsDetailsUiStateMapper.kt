package com.amsterdam.viewmodel.categoriesDetails.tvShow

import com.amsterdam.entity.TvShow
import com.amsterdam.viewmodel.shared.mappers.toFormattedRating
import com.amsterdam.viewmodel.utils.toYearString

fun TvShow.toTvShowUiState(): CategoriesTvShowsDetailsUiState.TvShowsUiState {
    return CategoriesTvShowsDetailsUiState.TvShowsUiState(
        id = id,
        name = name,
        posterImageUrl = posterUrl,
        rate = rating.toFormattedRating(),
        yearOfRelease = airDate.toYearString(),
        isAdult = isAdult
    )
}