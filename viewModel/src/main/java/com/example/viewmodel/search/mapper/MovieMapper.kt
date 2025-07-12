package com.example.viewmodel.search.mapper

import com.example.entity.Movie
import com.example.viewmodel.search.countrySearch.MovieUiState

val base_image_url = "https://image.tmdb.org/t/p/w500"

fun List<Movie>.toListOfUiState(): List<MovieUiState> {
    return this.map { it.toUiState() }
}

fun Movie.toUiState(): MovieUiState {
    return MovieUiState(
        id = this.id,
        name = this.name,
        poster = base_image_url + this.poster,
        productionYear = this.productionYear.toString(),
        rating = this.rating.toString(),
    )
}