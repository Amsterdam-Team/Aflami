package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.remote.RemoteMovieDetailsResponse
import com.amsterdam.repository.utils.toSafeLocalDate

fun RemoteMovieDetailsResponse.toLocalDto(storedLanguage: String): LocalMovieDto {
    return LocalMovieDto(
        movieId = id,
        storedLanguage = storedLanguage,
        name = title,
        description = overview,
        poster = posterPath.orEmpty(),
        releaseDate = releaseDate.toSafeLocalDate(),
        rating = voteAverage.toFloat(),
        popularity = popularity,
        movieLength = runtime,
        originCountry = originCountry.firstOrNull() ?: "",
    )
}