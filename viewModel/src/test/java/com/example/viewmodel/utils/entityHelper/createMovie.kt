package com.example.viewmodel.utils.entityHelper

import com.example.entity.Movie
import com.example.entity.category.MovieGenre

fun createMovie(
    id: Long = 1L,
    name: String = "Default Movie",
    description: String = "A sample movie description.",
    poster: String = "https://example.com/poster.jpg",
    productionYear: Int = 2024,
    genres: List<MovieGenre> = listOf(),
    rating: Float = 4.5f
): Movie {
    return Movie(
        id = id,
        name = name,
        description = description,
        poster = poster,
        productionYear = productionYear,
        categories = genres,
        rating = rating,
        popularity = 0.0,
        originCountry = "",
        movieLength = 110,
        hasVideo = false
    )
}