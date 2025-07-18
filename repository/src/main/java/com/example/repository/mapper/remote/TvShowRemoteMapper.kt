package com.example.repository.mapper.remote

import com.example.entity.TvShow
import com.example.entity.category.TvShowGenre
import com.example.repository.dto.remote.RemoteTvShowItemDto
import com.example.repository.mapper.shared.EntityMapper
import com.example.repository.mapper.shared.toTvShowCategory
import com.example.repository.utils.DateParser

class TvShowRemoteMapper(
    private val dateParser: DateParser
) : EntityMapper<RemoteTvShowItemDto, TvShow> {
    override fun toEntity(dto: RemoteTvShowItemDto): TvShow {
        return TvShow(
            id = dto.id,
            name = dto.title,
            description = dto.overview,
            posterUrl = dto.posterPath.orEmpty(),
            productionYear = dateParser.parseYear(dto.releaseDate).toUInt(),
            categories = mapGenreIdsToCategories(dto.genreIds),
            rating = dto.voteAverage.toFloat(),
            popularity = dto.popularity,
            seasonCount = dto.seasonCount,
            originCountry = dto.originCountry.firstOrNull() ?: "",
        )
    }

    private fun mapGenreIdsToCategories(genreIds: List<Int>): List<TvShowGenre> {
        return genreIds.map { it.toLong().toTvShowCategory() }
    }
}