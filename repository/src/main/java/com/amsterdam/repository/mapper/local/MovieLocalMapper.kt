package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.local.LocalMovieDto

fun LocalMovieDto.toMovieEntity(): Movie =
    Movie(
        id = movieId,
        name = name,
        description = description,
        posterUrl = poster,
        releaseDate = releaseDate,
        rating = rating,
        categories = emptyList(),
        popularity = popularity,
        runTimeInMinutes = movieLength,
        originCountry = originCountry,
        hasVideo = hasVideo,
        productionCompanies = emptyList()
    )

fun Movie.LocalMovieDto(storedLanguage: String): LocalMovieDto =
    LocalMovieDto(
        movieId = id,
        storedLanguage = storedLanguage,
        name = name,
        description = description,
        poster = posterUrl,
        releaseDate = releaseDate,
        rating = rating,
        popularity = popularity,
        movieLength = runTimeInMinutes,
        originCountry = originCountry,
        hasVideo = hasVideo
    )

