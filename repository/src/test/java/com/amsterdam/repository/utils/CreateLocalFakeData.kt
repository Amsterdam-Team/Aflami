package com.amsterdam.repository.utils

import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.local.MovieLocalDto
import kotlinx.datetime.LocalDate

val movieLocalDto1 = MovieLocalDto(
    movieId = 1,
    storedLanguage = "en",
    name = "Movie A",
    description = "Overview A",
    poster = "poster1.jpg",
    releaseDate = LocalDate(2022, 1, 1),
    popularity = 75.5,
    rating = 8.0f,
    originCountry = "USA",
    movieLength = 120,
    isAdult = false
)
val movieLocalDto2 = MovieLocalDto(
    movieId = 2,
    storedLanguage = "en",
    name = "Movie B",
    description = "Overview B",
    poster = "poster2.jpg",
    releaseDate = LocalDate(2022, 2, 1),
    popularity = 80.0,
    rating = 7.0f,
    originCountry = "UK",
    movieLength = 100,
    isAdult = false
    )
val movie1 = Movie(
    id = movieLocalDto1.movieId,
    name = movieLocalDto1.name,
    description = movieLocalDto1.description,
    posterUrl = movieLocalDto1.poster,
    releaseDate = movieLocalDto1.releaseDate,
    categories = emptyList(),
    rating = movieLocalDto1.rating,
    popularity = movieLocalDto1.popularity,
    originCountry = movieLocalDto1.originCountry,
    runTimeInMinutes = movieLocalDto1.movieLength,
)
val movie2 = Movie(
    id = movieLocalDto2.movieId,
    name = movieLocalDto2.name,
    description = movieLocalDto2.description,
    posterUrl = movieLocalDto2.poster,
    releaseDate = movieLocalDto2.releaseDate,
    categories = emptyList(),
    rating = movieLocalDto2.rating,
    popularity = movieLocalDto2.popularity,
    originCountry = movieLocalDto2.originCountry,
    runTimeInMinutes = movieLocalDto2.movieLength,
)