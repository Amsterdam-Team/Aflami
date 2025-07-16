package com.example.repository.mapper.remote

import com.example.entity.TvShow
import com.example.entity.category.TvShowGenre
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.remote.RemoteTvShowItemDto
import com.example.repository.dto.remote.RemoteTvShowResponse
import com.example.repository.mapper.shared.mapToTvShowCategory

class TvShowRemoteMapper {
    fun mapToTvShows(remoteTvShowResponse: RemoteTvShowResponse): List<TvShow> {
        return remoteTvShowResponse.results.map { mapToTvShow(it) }
    }

    fun mapToLocalTvShows(remoteTvShowResponse: RemoteTvShowResponse): List<LocalTvShowDto> {
        return remoteTvShowResponse.results.map { mapToLocalTvShow(it) }
    }

    fun mapToTvShow(remoteTvShowItemDto: RemoteTvShowItemDto): TvShow {
        return TvShow(
            id = remoteTvShowItemDto.id,
            name = remoteTvShowItemDto.title,
            description = remoteTvShowItemDto.overview,
            poster = remoteTvShowItemDto.posterPath.orEmpty(),
            productionYear = parseYear(remoteTvShowItemDto.releaseDate),
            categories = mapGenreIdsToCategories(remoteTvShowItemDto.genreIds),
            rating = remoteTvShowItemDto.voteAverage.toFloat(),
            popularity = remoteTvShowItemDto.popularity
        )
    }

    private fun mapToLocalTvShow(remoteTvShowItemDto: RemoteTvShowItemDto): LocalTvShowDto {
        return LocalTvShowDto(
            tvShowId = remoteTvShowItemDto.id,
            name = remoteTvShowItemDto.title,
            description = remoteTvShowItemDto.overview,
            poster = remoteTvShowItemDto.posterPath.orEmpty(),
            productionYear = parseYear(remoteTvShowItemDto.releaseDate),
            rating = remoteTvShowItemDto.voteAverage.toFloat(),
            popularity = remoteTvShowItemDto.popularity
        )
    }

    private fun mapGenreIdsToCategories(genreIds: List<Int>): List<TvShowGenre> {
        return genreIds.map { it.toLong().mapToTvShowCategory() }
    }

    private fun parseYear(date: String): Int {
        return date.takeIf { it.length >= 4 }?.substring(0, 4)?.toIntOrNull() ?: 0
    }
}