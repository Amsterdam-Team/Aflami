package com.example.repository.mapper.local

import com.example.entity.TvShow
import com.example.repository.dto.local.relation.TvShowWithCategory
import com.example.repository.mapper.shared.EntityMapper

class TvShowWithCategoryLocalMapper(
    private val tvShowGenreLocalMapper: TvShowGenreLocalMapper
) : EntityMapper<TvShowWithCategory, TvShow> {
    override fun toEntity(dto: TvShowWithCategory): TvShow {
        return TvShow(
            id = dto.tvShow.tvShowId,
            name = dto.tvShow.name,
            description = dto.tvShow.description,
            posterUrl = dto.tvShow.poster,
            productionYear = dto.tvShow.productionYear.toUInt(),
            rating = dto.tvShow.rating,
            categories = tvShowGenreLocalMapper.toEntityList(dto.categories),
            popularity = dto.tvShow.popularity,
            seasonCount = dto.tvShow.seasonCount,
            originCountry = dto.tvShow.originCountry,
        )
    }
}