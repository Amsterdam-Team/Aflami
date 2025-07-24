package com.example.repository.mapper.remote

import com.example.entity.TvShow
import com.example.entity.category.TvShowGenre
import com.example.repository.dto.remote.RemoteTvShowItemDto
import com.example.repository.mapper.shared.EntityMapper
import com.example.repository.mapper.shared.toTvShowCategory
import kotlinx.datetime.toLocalDate

class TvShowRemoteMapper() : EntityMapper<RemoteTvShowItemDto, TvShow> {
    override fun toEntity(dto: RemoteTvShowItemDto): TvShow {
        return TvShow(
            id = dto.id,
            name = dto.title,
            description = dto.overview,
            posterUrl = dto.fullPosterPath.orEmpty(),
            airDate = dto.releaseDate.toLocalDate(),
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