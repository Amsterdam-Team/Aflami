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
    rating: Float = 4.5f,
    originCountry: String = "USA",
    popularity : Double = 0.0,
    movieLength: Int = 120,
    hasVideo: Boolean = true
): Movie {
    return Movie(
        id = id,
        name = name,
        description = description,
        poster = poster,
        productionYear = productionYear,
        categories = genres,
        rating = rating,
        popularity = popularity,
        originCountry = originCountry,
        movieLength = movieLength,
        hasVideo = hasVideo
    )
}