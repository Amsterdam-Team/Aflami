package com.example.repository.mapper.remote

import com.example.entity.TvShow
import com.example.repository.dto.remote.TvShowDetailsRemoteResponse
import com.example.repository.mapper.shared.EntityMapper
import com.example.repository.mapper.shared.toTvShowCategory
import com.example.repository.utils.DateParser

class TvShowDetailsRemoteMapper(
    private val dateParser: DateParser,
) : EntityMapper<TvShowDetailsRemoteResponse, TvShow> {
    override fun toEntity(dto: TvShowDetailsRemoteResponse): TvShow {
        return TvShow(
            id = dto.id,
            name = dto.title,
            description = dto.overview,
            posterUrl = dto.fullPosterPath.orEmpty(),
            productionYear = dateParser.parseYear(dto.releaseDate.toString()).toUInt(),
            categories = dto.genres.map { it.id.toLong().toTvShowCategory() },
            rating = dto.voteAverage.toFloat(),
            popularity = dto.popularity,
            seasonCount = dto.seasonCount,
            originCountry = dto.originCountry.firstOrNull() ?: ""
        )
    }
}