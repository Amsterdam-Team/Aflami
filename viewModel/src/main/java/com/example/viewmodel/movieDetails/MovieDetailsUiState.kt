package com.example.viewmodel.movieDetails

import com.example.entity.category.MovieGenre
import com.example.viewmodel.shared.Selectable
import com.example.viewmodel.shared.movieAndSeriseDetails.ActorUiState
import com.example.viewmodel.shared.movieAndSeriseDetails.ProductionCompanyUiState
import com.example.viewmodel.shared.movieAndSeriseDetails.ReviewUiState
import com.example.viewmodel.shared.movieAndSeriseDetails.SimilarMovieUiState

data class MovieDetailsUiState(
    val movieId: Long = 0,
    val rating: String = "",
    val movieTitle: String = "",
    val categories: List<MovieGenre> = emptyList(),
    val releaseDate: String = "",
    val movieLength: String = "",
    val originCountry: String = "",
    val description: String = "",
    val hasVideo : Boolean = false,
    val moviePostersUrl : List<String> = emptyList(),
    val actors: List<ActorUiState> = emptyList(),
    val extraItem: List<Selectable<MovieExtras>> = listOf(
        Selectable(isSelected = true, MovieExtras.MORE_LIKE_THIS),
        Selectable(isSelected = false, MovieExtras.REVIEWS),
        Selectable(isSelected = false, MovieExtras.GALLERY),
        Selectable(isSelected = false, MovieExtras.COMPANY_PRODUCTION)
    ),
    val similarMovies: List<SimilarMovieUiState> = emptyList(),
    val productionCompany: List<ProductionCompanyUiState> = emptyList(),
    val gallery: List<String> = emptyList(),
    val reviews: List<ReviewUiState> = emptyList(),
    val isLoading: Boolean = false,
    val networkError : Boolean = false
) {

    enum class MovieExtras {
        MORE_LIKE_THIS,
        REVIEWS,
        GALLERY,
        COMPANY_PRODUCTION
    }
}