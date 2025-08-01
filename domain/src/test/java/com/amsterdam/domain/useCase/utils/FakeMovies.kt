package com.amsterdam.domain.useCase.utils

import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre
import kotlinx.datetime.LocalDate

val specificMovieList = listOf(
    Movie(
        id = 1,
        name = "Low Rated",
        description = "",
        posterUrl = "",
        releaseDate = LocalDate(2023, 1, 1),
        categories = listOf(),
        rating = 1.0f,
        popularity = 5.0,
        originCountry = "",
        runTimeInMinutes = 1,
        hasVideo = true,
        productionCompanies = emptyList()
    ),
    Movie(
        id = 2,
        name = "Wrong Category",
        description = "",
        posterUrl = "",
        releaseDate = LocalDate(2023, 1, 1),
        categories = listOf(MovieGenre.TV_MOVIE),
        rating = 5.0f,
        popularity = 5.0,
        originCountry = "",
        runTimeInMinutes = 1,
        hasVideo = true,
        productionCompanies = emptyList()
    )
)

val fakeMovieListWithCategories =
    listOf(
        Movie(
            id = 1,
            name = "Action Movie",
            description = "",
            posterUrl = "",
            releaseDate = LocalDate(2023, 1, 1),
            categories = listOf(MovieGenre.ACTION),
            rating = 8.0f,
            popularity = 10.0,
            originCountry = "",
            runTimeInMinutes = 1,
            hasVideo = true,
            productionCompanies = emptyList()
        ),
        Movie(
            id = 2,
            name = "Comedy Movie",
            description = "",
            posterUrl = "",
            releaseDate = LocalDate(2023, 1, 1),
            categories = listOf(MovieGenre.COMEDY),
            rating = 7.0f,
            popularity = 9.0,
            originCountry = "",
            runTimeInMinutes = 1,
            hasVideo = true,
            productionCompanies = emptyList()
        ),
        Movie(
            id = 3,
            name = "Action & Drama",
            description = "",
            posterUrl = "",
            releaseDate = LocalDate(2023, 1, 1),
            categories = listOf(
                MovieGenre.ACTION,
                MovieGenre.DRAMA,
            ),
            rating = 7.5f,
            popularity = 11.0,
            originCountry = "",
            runTimeInMinutes = 1,
            hasVideo = true,
            productionCompanies = emptyList()
        ),
        Movie(
            id = 4,
            name = "Thriller",
            description = "",
            posterUrl = "",
            releaseDate = LocalDate(2023, 1, 1),
            categories = listOf(MovieGenre.THRILLER),
            rating = 6.0f,
            popularity = 8.0,
            originCountry = "",
            runTimeInMinutes = 1,
            hasVideo = true,
            productionCompanies = emptyList()
        )
    )

val fakeMovieList =
    listOf(
        Movie(
            id = 1,
            name = "abc",
            description = "",
            posterUrl = "",
            releaseDate = LocalDate(2023, 1, 1),
            categories = emptyList(),
            rating = 2.5f,
            popularity = 10.2,
            originCountry = "",
            runTimeInMinutes = 1,
            hasVideo = true,
            productionCompanies = emptyList()
        ),
        Movie(
            id = 2,
            name = "dfg",
            description = "",
            posterUrl = "",
            releaseDate = LocalDate(2023, 1, 1),
            categories = emptyList(),
            rating = 2.5f,
            popularity = 11.2,
            originCountry = "",
            runTimeInMinutes = 1,
            hasVideo = true,
            productionCompanies = emptyList()
        ),
        Movie(
            id = 3,
            name = "hij",
            description = "",
            posterUrl = "",
            releaseDate = LocalDate(2023, 1, 1),
            categories = emptyList(),
            rating = 2.5f,
            popularity = 0.2,
            originCountry = "",
            runTimeInMinutes = 1,
            hasVideo = true,
            productionCompanies = emptyList()
        ),
    )
val fakeMovieListWithRatings =
    listOf(
        Movie(
            id = 1,
            name = "High Rated",
            description = "",
            posterUrl = "",
            releaseDate = LocalDate(2023, 1, 1),
            categories = emptyList(),
            rating = 8.0f,
            popularity = 10.0,
            originCountry = "",
            runTimeInMinutes = 1,
            hasVideo = true,
            productionCompanies = emptyList()
        ),
        Movie(
            id = 2,
            name = "Medium Rated",
            description = "",
            posterUrl = "",
            releaseDate = LocalDate(2023, 1, 1),
            categories = emptyList(),
            rating = 5.5f,
            popularity = 9.0,
            originCountry = "",
            runTimeInMinutes = 1,
            hasVideo = true,
            productionCompanies = emptyList()
        ),
        Movie(
            id = 3,
            name = "Low Rated",
            description = "",
            posterUrl = "",
            releaseDate = LocalDate(2023, 1, 1),
            categories = emptyList(),
            rating = 3.0f,
            popularity = 8.0,
            originCountry = "",
            runTimeInMinutes = 1,
            hasVideo = true,
            productionCompanies = emptyList()
        ),
    )