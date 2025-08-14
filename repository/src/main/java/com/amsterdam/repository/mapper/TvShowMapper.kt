package com.amsterdam.repository.mapper

import com.amsterdam.entity.TvShow
import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.remote.RemoteTvShowItemDto
import com.amsterdam.repository.utils.toSafeLocalDate

fun TvShowLocalDto.toEntity(): TvShow =
    TvShow(
        id = tvShowId,
        name = name,
        description = description,
        posterUrl = poster,
        airDate = airDate,
        rating = rating,
        categories = emptyList(),
        popularity = popularity,
        seasonCount = seasonCount,
        originCountry = originCountry
    )

fun RemoteTvShowItemDto.toEntity(
    isPoster: Boolean = true
): TvShow {
    val imageUrl = if (isPoster) fullPosterPath else fullBackdropPath
    return TvShow(
        id = id,
        name = title,
        description = overview,
        posterUrl = imageUrl.orEmpty(),
        airDate = releaseDate.toSafeLocalDate(),
        categories = genreIds.map { toTvShowGenre(it.toLong()) },
        rating = voteAverage.toFloat(),
        popularity = popularity,
        seasonCount = seasonCount,
        originCountry = originCountry.firstOrNull() ?: ""
    )
}

fun List<RemoteTvShowItemDto>.toEntityList(isPoster: Boolean = true): List<TvShow> =
    map { it.toEntity(isPoster) }

fun RemoteTvShowItemDto.toLocalDto(storedLanguage: String): TvShowLocalDto {
    return TvShowLocalDto(
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

fun List<RemoteTvShowItemDto>.toLocalTvShowDtoList(storedLanguage: String) = map { it.toLocalDto(storedLanguage) }