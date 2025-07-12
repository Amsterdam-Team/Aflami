package com.example.viewmodel.search.mapper

import com.example.entity.Movie
import com.example.viewmodel.search.countrySearch.MovieUiState

fun List<Movie>.toListOfUiState(): List<MovieUiState> {
    return this.map { it.toUiState() }
}

fun Movie.toUiState(): MovieUiState {
    return MovieUiState(
        id = this.id,
        name = this.name,
        poster = this.poster,
        productionYear = this.productionYear.toString(),
        rating = this.rating.toString(),
    )
}