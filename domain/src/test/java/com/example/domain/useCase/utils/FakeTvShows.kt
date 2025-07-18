package com.example.domain.useCase.utils

import com.example.entity.TvShow
import com.example.entity.category.TvShowGenre

val specificTvShowList = listOf(
    TvShow(
        id = 1,
        name = "Low Rated",
        description = "",
        posterUrl = "",
        productionYear = (2023).toUInt(),
        categories = listOf(),
        rating = 1.0f,
        popularity = 5.0
    ),
    TvShow(
        id = 2,
        name = "Wrong Category",
        description = "",
        posterUrl = "",
        productionYear = (2023).toUInt(),
        categories = listOf(
            TvShowGenre.TALK
        ),
        rating = 5.0f,
        popularity = 5.0
    )
)

val fakeTvShowList =
    listOf(
        TvShow(
            id = 1,
            name = "abc",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = emptyList(),
            rating = 2.5f,
            popularity = 10.2,
        ),
        TvShow(
            id = 2,
            name = "dfg",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = emptyList(),
            rating = 2.5f,
            popularity = 11.2,
        ),
        TvShow(
            id = 3,
            name = "hij",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = emptyList(),
            rating = 2.5f,
            popularity = 0.2,
        ),
    )
val fakeTvShowListWithRatings =
    listOf(
        TvShow(
            id = 1,
            name = "High Rated",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = emptyList(),
            rating = 8.0f,
            popularity = 10.0
        ),
        TvShow(
            id = 2,
            name = "Medium Rated",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = emptyList(),
            rating = 5.5f,
            popularity = 9.0
        ),
        TvShow(
            id = 3,
            name = "Low Rated",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = emptyList(),
            rating = 3.0f,
            popularity = 8.0
        ),
    )
val fakeTvShowListWithCategories =
    listOf(
        TvShow(
            id = 1,
            name = "Action Show",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = listOf(TvShowGenre.TALK),
            rating = 8.0f,
            popularity = 10.0
        ),
        TvShow(
            id = 2,
            name = "Comedy Show",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = listOf(TvShowGenre.TALK),
            rating = 7.0f,
            popularity = 9.0
        ),
        TvShow(
            id = 3,
            name = "Action & Drama",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = listOf(
                TvShowGenre.TALK,
                TvShowGenre.COMEDY,
            ),
            rating = 7.5f,
            popularity = 11.0
        ),
        TvShow(
            id = 4,
            name = "Thriller",
            description = "",
            posterUrl = "",
            productionYear = (2023).toUInt(),
            categories = listOf(TvShowGenre.TALK),
            rating = 6.0f,
            popularity = 8.0
        )
    )