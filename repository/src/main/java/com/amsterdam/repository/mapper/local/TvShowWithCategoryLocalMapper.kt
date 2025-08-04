package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.TvShow
import com.amsterdam.repository.dto.local.relation.TvShowWithCategory
import com.amsterdam.repository.mapper.shared.EntityMapper
import javax.inject.Inject

class TvShowWithCategoryLocalMapper @Inject constructor(
    private val tvShowGenreLocalMapper: TvShowGenreLocalMapper
) : EntityMapper<TvShowWithCategory, TvShow> {
    override fun toEntity(dto: TvShowWithCategory): TvShow {
        return TvShow(
            id = dto.tvShow.tvShowId,
            name = dto.tvShow.name,
            description = dto.tvShow.description,
            posterUrl = dto.tvShow.poster,
            airDate = dto.tvShow.airDate,
            rating = dto.tvShow.rating,
            categories = tvShowGenreLocalMapper.toEntityList(dto.categories.distinctBy { it.categoryId }),
            popularity = dto.tvShow.popularity,
            seasonCount = dto.tvShow.seasonCount,
            originCountry = dto.tvShow.originCountry,
            productionCompanies = emptyList()
        )
    }
}