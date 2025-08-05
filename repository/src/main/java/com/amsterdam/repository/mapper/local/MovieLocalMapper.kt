package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.local.LocalMovieDto

fun LocalMovieDto.toEntity(): Movie =
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
        originCountry = originCountry
    )

