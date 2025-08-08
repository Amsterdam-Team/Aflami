package com.amsterdam.viewmodel.utils.entityHelper

import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre
import kotlinx.datetime.LocalDate

fun createMovie(
    id: Long = 0L,
    name: String = "",
    description: String = "",
    poster: String = "",
    productionYear: Int = 0,
    genres: List<MovieGenre> = emptyList(),
    rating: Float = 0f,
    originCountry: String = "",
    popularity: Double = 0.0,
    runTime: Int = 0,
): Movie {
    return Movie(
        id = id,
        name = name,
        description = description,
        posterUrl = poster,
        categories = genres,
        rating = rating,
        popularity = popularity,
        originCountry = originCountry,
        runTimeInMinutes = runTime,
        releaseDate = LocalDate(productionYear, 1, 1)
    )
}
