package com.example.localdatasource

import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.dto.local.relation.TvShowWithCategory

val expectedMovieWithCategories = listOf(
    MovieWithCategories(
        movie = LocalMovieDto(
            1,
            name = "",
            description = "",
            poster = "",
            productionYear = 2,
            popularity = 22.2,
            rating = 9.9f,
            originCountry ="",
            movieLength = 2,
            hasVideo = false,
        ), categories = listOf()
    )
)
val expectedMovieDto = listOf(
    LocalMovieDto(
        1,
        name = "",
        description = "",
        poster = "",
        productionYear = 2,
        popularity = 22.2,
        rating = 9.9f,
        originCountry = "",
        movieLength = 3,
        hasVideo = false,
    )
)
val expectedTvShowWithCategory = listOf(
    TvShowWithCategory(
        tvShow = LocalTvShowDto(
            tvShowId = 1,
            name = "",
            description = "",
            poster = "",
            productionYear = 2,
            popularity = 22.2,
            rating = 9.9f,
        ),
        categories = emptyList()
    )
)
val tvShows = listOf(
    LocalTvShowDto(
        tvShowId = 1,
        name = "",
        description = "",
        poster = "",
        productionYear = 2,
        popularity = 22.2,
        rating = 9.9f,
    )
)