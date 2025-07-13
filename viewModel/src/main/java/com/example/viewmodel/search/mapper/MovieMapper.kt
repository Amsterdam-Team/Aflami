package com.example.viewmodel.search.mapper

import com.example.entity.Movie
import com.example.viewmodel.search.countrySearch.MovieUiState
import java.text.DecimalFormat

val base_image_url = "https://image.tmdb.org/t/p/w500"

fun List<Movie>.toListOfUiState(): List<MovieUiState> {
    return this.map { it.toUiState() }
}

fun Movie.toUiState(): MovieUiState {
    return MovieUiState(
        id = id,
        name = name,
        poster = base_image_url + poster,
        productionYear = productionYear.toString(),
        rating = DecimalFormat("#.#").format(rating),
    )
}