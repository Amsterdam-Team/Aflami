package com.example.repository.mapper.remote

import com.example.entity.TvShow
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.remote.RemoteTvShowItemDto
import com.example.repository.dto.remote.RemoteTvShowResponse
import com.example.repository.mapper.shared.toTvShowCategory

fun RemoteTvShowResponse.toTvShows(): List<TvShow> {
    return results.map { it.toTvShow() }
}

fun RemoteTvShowResponse.toLocalTvShows(): List<LocalTvShowDto> {
    return results.map { it.toLocalTvShow() }
}

fun RemoteTvShowItemDto.toTvShow(): TvShow {
    return TvShow(
        id = id,
        name = title,
        description = overview,
        poster = posterPath.orEmpty(),
        productionYear = releaseDate.parseYear(),
        categories = genreIds.map { it.toLong().toTvShowCategory() },
        rating = voteAverage.toFloat(),
        popularity = popularity
    )
}

fun RemoteTvShowItemDto.toLocalTvShow(): LocalTvShowDto {
    return LocalTvShowDto(
        tvShowId = id,
        name = title,
        description = overview,
        poster = posterPath.orEmpty(),
        productionYear = releaseDate.parseYear(),
        rating = voteAverage.toFloat(),
        popularity = popularity
    )
}

private fun String.parseYear(): Int {
    return takeIf { length >= 4 }?.substring(0, 4)?.toIntOrNull() ?: 0
}
