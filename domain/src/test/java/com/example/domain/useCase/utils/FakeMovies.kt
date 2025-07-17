package com.example.domain.useCase.utils

import com.example.entity.Movie
import com.example.entity.category.MovieGenre

val specificMovieList = listOf(
    Movie(
        id = 1,
        name = "Low Rated",
        description = "",
        posterUrl = "",
        productionYear = (2023).toUInt(),
        categories = listOf(),
        rating = 1.0f,
        popularity = 5.0,
        originCountry = "",
        runTime = 1,
        hasVideo = true
    ),
    Movie(
        id = 2,
        name = "Wrong Category",
        description = "",
        posterUrl = "",
        productionYear = (2023).toUInt(),
        categories = listOf(MovieGenre.TV_MOVIE),
        rating = 5.0f,
        popularity = 5.0,
        originCountry = "",
        runTime = 1,
        hasVideo = true
    )
)

val fakeMovieListWithCategories =
    listOf(
        Movie(
            id = 1,
            name = "Action Movie",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = listOf(MovieGenre.ACTION),
            rating = 8.0f,
            popularity = 10.0,
            originCountry = "",
            runTime = 1,
            hasVideo = true
        ),
        Movie(
            id = 2,
            name = "Comedy Movie",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = listOf(MovieGenre.COMEDY),
            rating = 7.0f,
            popularity = 9.0,
            originCountry = "",
            runTime = 1,
            hasVideo = true
        ),
        Movie(
            id = 3,
            name = "Action & Drama",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = listOf(
                MovieGenre.ACTION,
                MovieGenre.DRAMA,
            ),
            rating = 7.5f,
            popularity = 11.0,
            originCountry = "",
            runTime = 1,
            hasVideo = true
        ),
        Movie(
            id = 4,
            name = "Thriller",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = listOf(MovieGenre.THRILLER),
            rating = 6.0f,
            popularity = 8.0,
            originCountry = "",
            runTime = 1,
            hasVideo = true
        )
    )


val fakeMovieList =
    listOf(
        Movie(
            id = 1,
            name = "abc",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = emptyList(),
            rating = 2.5f,
            popularity = 10.2,
            originCountry = "",
            runTime = 1,
            hasVideo = true
        ),
        Movie(
            id = 2,
            name = "dfg",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = emptyList(),
            rating = 2.5f,
            popularity = 11.2,
            originCountry = "",
            runTime = 1,
            hasVideo = true
        ),
        Movie(
            id = 3,
            name = "hij",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = emptyList(),
            rating = 2.5f,
            popularity = 0.2,
            originCountry = "",
            runTime = 1,
            hasVideo = true
        ),
    )
val fakeMovieListWithRatings =
    listOf(
        Movie(
            id = 1,
            name = "High Rated",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = emptyList(),
            rating = 8.0f,
            popularity = 10.0,
            originCountry = "",
            runTime = 1,
            hasVideo = true
        ),
        Movie(
            id = 2,
            name = "Medium Rated",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = emptyList(),
            rating = 5.5f,
            popularity = 9.0,
            originCountry = "",
            runTime = 1,
            hasVideo = true
        ),  // Rounds to 6
        Movie(
            id = 3,
            name = "Low Rated",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = emptyList(),
            rating = 3.0f,
            popularity = 8.0,
            originCountry = "",
            runTime = 1,
            hasVideo = true
        ),
    )
