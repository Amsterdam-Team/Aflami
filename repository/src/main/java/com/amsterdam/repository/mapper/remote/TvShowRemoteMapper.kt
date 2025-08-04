package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.TvShow
import com.amsterdam.repository.dto.remote.RemoteTvShowItemDto
import com.amsterdam.repository.mapper.shared.toTvShowGenre
import com.amsterdam.repository.utils.toSafeLocalDate

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