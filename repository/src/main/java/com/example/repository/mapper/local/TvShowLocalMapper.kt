package com.example.repository.mapper.local

import com.example.domain.mapper.DomainMapper
import com.example.domain.mapper.DtoMapper
import com.example.entity.TvShow
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.relation.TvShowWithCategory

class TvShowLocalMapper(
    private val categoryLocalMapper: CategoryLocalMapper
) : DomainMapper<TvShow, LocalTvShowDto>, DtoMapper<TvShow, LocalTvShowDto> {

    fun toTvShows(tvShowWithCategories: List<TvShowWithCategory>): List<TvShow> {
        return tvShowWithCategories.map { toTvShow(it) }
    }

    fun toTvShow(tvShowWithCategory: TvShowWithCategory): TvShow {
        return TvShow(
            id = tvShowWithCategory.tvShow.tvShowId,
            name = tvShowWithCategory.tvShow.name,
            description = tvShowWithCategory.tvShow.description,
            poster = tvShowWithCategory.tvShow.poster,
            productionYear = tvShowWithCategory.tvShow.productionYear,
            rating = tvShowWithCategory.tvShow.rating,
            categories = categoryLocalMapper.toTvShowCategories(tvShowWithCategory.categories),
            popularity = tvShowWithCategory.tvShow.popularity
        )
    }

    override fun toDomain(dto: LocalTvShowDto): TvShow {
        return TvShow(
            id = dto.tvShowId,
            name = dto.name,
            description = dto.description,
            poster = dto.poster,
            productionYear = dto.productionYear,
            rating = dto.rating,
            categories = emptyList(),
            popularity = dto.popularity
        )
    }

    override fun toDto(domain: TvShow): LocalTvShowDto {
        return LocalTvShowDto(
            tvShowId = domain.id,
            name = domain.name,
            description = domain.description,
            poster = domain.poster,
            productionYear = domain.productionYear,
            rating = domain.rating,
            popularity = domain.popularity
        )
    }
}