package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.utils.toSafeLocalDate

fun TvShowDetailsRemoteResponse.toLocalDto(storedLanguage: String): LocalTvShowDto {
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
