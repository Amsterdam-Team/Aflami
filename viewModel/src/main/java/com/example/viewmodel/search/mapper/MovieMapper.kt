package com.example.viewmodel.search.mapper

import com.example.entity.Movie
import com.example.viewmodel.search.countrySearch.MovieUiState
import java.text.DecimalFormat

fun List<Movie>.toListOfUiState(): List<MovieUiState> {
    return this.map { it.toUiState() }
}

fun Movie.toUiState(): MovieUiState {
    return MovieUiState(
        id = id,
        name = name,
        posterImageUrl = poster,
        productionYear = productionYear.toString(),
        rating = DecimalFormat("#.#").format(rating),
    )
}