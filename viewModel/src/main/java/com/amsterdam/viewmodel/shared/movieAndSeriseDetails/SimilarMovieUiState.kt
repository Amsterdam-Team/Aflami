package com.amsterdam.viewmodel.shared.movieAndSeriseDetails

data class SimilarMovieUiState(
    val movieId: Long,
    val rate: String = "",
    val name: String = "",
    val productionYear: String = "",
    val posterUrl: String = ""
)