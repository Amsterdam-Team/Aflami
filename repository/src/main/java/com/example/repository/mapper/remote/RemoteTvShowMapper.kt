package com.example.repository.mapper.remote

import com.example.entity.Category
import com.example.entity.TvShow
import com.example.repository.dto.remote.RemoteTvShowItemDto
import com.example.repository.dto.remote.RemoteTvShowResponse

class RemoteTvShowMapper {

    fun mapToDomain(dto: RemoteTvShowItemDto): TvShow {
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

    fun mapResponseToDomain(response: RemoteTvShowResponse): List<TvShow> {
        return response.results.map { mapToDomain(it) }
    }

    private fun parseYear(date: String): Int {
        return date.takeIf { it.length >= 4 }?.substring(0, 4)?.toIntOrNull() ?: 0
    }

    private fun mapGenreIdsToCategories(genreIds: List<Int>): List<Category> {
        return genreIds.map { Category(id = it.toLong(), name = "", image = "") }
    }
}