package com.example.viewmodel.movieDetails

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MovieDetailsUiState(
    val movieId: Long = 0,
    val imageUrl: String = "https://image.tmdb.org/t/p/w500/1GJvBE7UWU1WOVi0XREl4JQc7f8.jpg",
    val rating: String = "9.9",
    val movieTitle: String = "The Green Mile ",
    val categoriesNames: List<String> = listOf("Drama", "Family", "Comedy", "Fantasy"),
    val releaseDate: String = "2016-10-12",
    val movieLength: String = "1h 32m",
    val originCountry: String = "US",
    val description: String = "In 1935, corrections officer Paul Edgecomb oversees \"The Green Mile,\" the death row section of Cold Mountain Penitentiary, alongside officers Brutus Howell, Dean Stanton, Harry Terwilliger, and the sadistic and despised Percy Wetmore, whose connections to the state governor shield him from punishment. ",
    val actors: List<ActorUiState> = listOf(
        ActorUiState(),
        ActorUiState(),
        ActorUiState(),
        ActorUiState(),
        ActorUiState(),
        ActorUiState()
    ),
    val extraItem: List<Selectable<Extras>> = listOf(
        Selectable(isSelected = true, Extras.MORE_LIKE_THIS),
        Selectable(isSelected = false, Extras.REVIEWS),
        Selectable(isSelected = false, Extras.GALLERY),
        Selectable(isSelected = false, Extras.COMPANY_PRODUCTION)
    )
)


data class Selectable<T>(
    val isSelected: Boolean,
    val extras: T
)

enum class Extras {
    MORE_LIKE_THIS,
    REVIEWS,
    GALLERY,
    COMPANY_PRODUCTION
}

data class ActorUiState(
    val photo: String = "https://image.tmdb.org/t/p/w500/1GJvBE7UWU1WOVi0XREl4JQc7f8.jpg",
    val name: String = "Tom Hanks"
)
