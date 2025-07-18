package com.example.repository.mapper.remote

import com.example.domain.mapper.DomainMapper
import com.example.entity.TvShow
import com.example.entity.category.TvShowGenre
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.remote.RemoteTvShowItemDto
import com.example.repository.dto.remote.RemoteTvShowResponse
import com.example.repository.mapper.shared.toTvShowCategory

class TvShowRemoteMapper: DomainMapper<TvShow, RemoteTvShowItemDto> {
    fun toTvShows(remoteTvShowResponse: RemoteTvShowResponse): List<TvShow> {
        return remoteTvShowResponse.results.map { toDomain(it) }
    }

    fun toLocalTvShows(remoteTvShowResponse: RemoteTvShowResponse): List<LocalTvShowDto> {
        return remoteTvShowResponse.results.map { toLocalTvShow(it) }
    }

    private fun toLocalTvShow(remoteTvShowItemDto: RemoteTvShowItemDto): LocalTvShowDto {
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
        return genreIds.map { it.toLong().toTvShowCategory() }
    }

    private fun parseYear(date: String): Int {
        return date.takeIf { it.length >= 4 }?.substring(0, 4)?.toIntOrNull() ?: 0
    }

    override fun toDomain(dto: RemoteTvShowItemDto): TvShow {
        return TvShow(
            id = dto.id,
            name = dto.title,
            description = dto.overview,
            poster = dto.posterPath.orEmpty(),
            productionYear = parseYear(dto.releaseDate),
            categories = mapGenreIdsToCategories(dto.genreIds),
            rating = dto.voteAverage.toFloat(),
            popularity = dto.popularity
        )
    }
}