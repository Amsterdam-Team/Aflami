package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.utils.toSafeLocalDate

fun RemoteMovieItemDto.toLocalDto(isPoster : Boolean = true, storedLanguage: String): LocalMovieDto {
    val imageUrl = if (isPoster) fullPosterUrl else fullBackdropUrl

    return LocalMovieDto(
        movieId = id,
        storedLanguage = storedLanguage,
        name = title,
        description = overview,
        poster = imageUrl.orEmpty(),
        releaseDate = releaseDate.toSafeLocalDate(),
        rating = voteAverage.toFloat(),
        popularity = popularity,
        movieLength = runtime,
        originCountry = originCountry.firstOrNull() ?: ""
    )
}

fun List<RemoteMovieItemDto>.toLocalMovieDtoList(isPoster : Boolean = true,storedLanguage: String) =
    map { it.toLocalDto(isPoster,storedLanguage) }