package com.amsterdam.domain.useCase.utils

import com.amsterdam.entity.TvShow
import com.amsterdam.entity.category.TvShowGenre
import kotlinx.datetime.LocalDate

val specificTvShowList = listOf(
    TvShow(
        id = 1,
        name = "Low Rated",
        description = "",
        posterUrl = "",
        airDate = LocalDate(2023, 1, 1),
        categories = listOf(),
        rating = 1.0f,
        popularity = 5.0,
        seasonCount = 3,
        originCountry = "US",
    ),
    TvShow(
        id = 2,
        name = "Wrong Category",
        description = "",
        posterUrl = "",
        airDate = LocalDate(2023, 1, 1),
        categories = listOf(
            TvShowGenre.TALK
        ),
        rating = 5.0f,
        popularity = 5.0,
        seasonCount = 4,
        originCountry = "US",
    )
)

val fakeTvShowList =
    listOf(
        TvShow(
            id = 1,
            name = "abc",
            description = "",
            posterUrl = "",
            airDate = LocalDate(2023, 1, 1),
            categories = emptyList(),
            rating = 2.5f,
            popularity = 10.2,
            seasonCount = 4,
            originCountry = "US",
        ),
        TvShow(
            id = 2,
            name = "dfg",
            description = "",
            posterUrl = "",
            airDate = LocalDate(2023, 1, 1),
            categories = emptyList(),
            rating = 2.5f,
            popularity = 11.2,
            seasonCount = 4,
            originCountry = "US",
        ),
        TvShow(
            id = 3,
            name = "hij",
            description = "",
            posterUrl = "",
            airDate = LocalDate(2023, 1, 1),
            categories = emptyList(),
            rating = 2.5f,
            popularity = 0.2,
            seasonCount = 4,
            originCountry = "US",
        ),
    )
val fakeTvShowListWithRatings =
    listOf(
        TvShow(
            id = 1,
            name = "High Rated",
            description = "",
            posterUrl = "",
            airDate = LocalDate(2023, 1, 1),
            categories = emptyList(),
            rating = 8.0f,
            popularity = 10.0,
            seasonCount = 4,
            originCountry = "US",
           
        ),
        TvShow(
            id = 2,
            name = "Medium Rated",
            description = "",
            posterUrl = "",
            airDate = LocalDate(2023, 1, 1),
            categories = emptyList(),
            rating = 5.5f,
            popularity = 9.0,
            seasonCount = 4,
            originCountry = "US",
           
        ),
        TvShow(
            id = 3,
            name = "Low Rated",
            description = "",
            posterUrl = "",
            airDate = LocalDate(2023, 1, 1),
            categories = emptyList(),
            rating = 3.0f,
            popularity = 8.0,
            seasonCount = 4,
            originCountry = "US",
        ),
    )
val fakeTvShowListWithCategories =
    listOf(
        TvShow(
            id = 1,
            name = "Action Show",
            description = "",
            posterUrl = "",
            airDate = LocalDate(2023, 1, 1),
            categories = listOf(TvShowGenre.TALK),
            rating = 8.0f,
            popularity = 10.0,
            seasonCount = 4,
            originCountry = "US",
        ),
        TvShow(
            id = 2,
            name = "Comedy Show",
            description = "",
            posterUrl = "",
            airDate = LocalDate(2023, 1, 1),
            categories = listOf(TvShowGenre.TALK),
            rating = 7.0f,
            popularity = 9.0,
            seasonCount = 4,
            originCountry = "US",
        ),
        TvShow(
            id = 3,
            name = "Action & Drama",
            description = "",
            posterUrl = "",
            airDate = LocalDate(2023, 1, 1),
            categories = listOf(
                TvShowGenre.TALK,
                TvShowGenre.COMEDY,
            ),
            rating = 7.5f,
            popularity = 11.0,
            seasonCount = 4,
            originCountry = "US",
        ),
        TvShow(
            id = 4,
            name = "Thriller",
            description = "",
            posterUrl = "",
            airDate = LocalDate(2023, 1, 1),
            categories = listOf(TvShowGenre.TALK),
            rating = 6.0f,
            popularity = 8.0,
            seasonCount = 4,
            originCountry = "US",
        )
    )
val tvShow1=TvShow(
    id = 1,
    name = "hij",
    description = "",
    posterUrl = "",
    airDate = LocalDate(2023, 1, 1),
    categories = emptyList(),
    rating = 2.5f,
    popularity = 0.2,
    seasonCount = 4,
    originCountry = "US",
)