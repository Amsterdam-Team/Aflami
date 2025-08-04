package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.remote.RemoteTvShowItemDto
import com.amsterdam.repository.utils.toSafeLocalDate

fun RemoteTvShowItemDto.toLocalTvShowDto(storedLanguage: String): LocalTvShowDto {
    return LocalTvShowDto(
        tvShowId = id,
        storedLanguage = storedLanguage,
        name = title,
        description = overview,
        poster = fullPosterPath.orEmpty(),
        airDate = releaseDate.toSafeLocalDate(),
        rating = voteAverage.toFloat(),
        popularity = popularity,
        seasonCount = seasonCount,
        originCountry = originCountry.firstOrNull() ?: "",
    )
}

fun List<RemoteTvShowItemDto>.toLocalTvShowDtoList(storedLanguage: String) = map { it.toLocalTvShowDto(storedLanguage) }