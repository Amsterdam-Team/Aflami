package com.example.viewmodel.movieDetails

import com.example.entity.category.MovieGenre
import com.example.viewmodel.shared.Selectable

data class MovieDetailsUiState(
    val movieId: Long = 0,
    val posterUrl: String = "",
    val rating: String = "",
    val movieTitle: String = "",
    val categories: List<MovieGenre> = emptyList(),
    val releaseDate: String = "",
    val movieLength: String = "",
    val originCountry: String = "",
    val description: String = "",
    val hasVideo : Boolean = false,
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

    data class ActorUiState(
        val photo: String = "",
        val name: String = ""
    )

    data class SimilarMovieUiState(
        val rate: String = "",
        val name: String = "",
        val productionYear: String = "",
        val posterUrl: String = ""
    )

    data class ProductionCompanyUiState(
        val image: String = "",
        val name: String = "",
        val country: String = ""
    )

    data class ReviewUiState(
        val author: String = "",
        val username: String = "",
        val rating: String = "",
        val content: String = "",
        val date: String = "",
        val imageUrl: String? = "",
    )
}