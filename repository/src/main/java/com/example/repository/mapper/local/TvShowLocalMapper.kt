package com.example.repository.mapper.local

import com.example.entity.TvShow
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.relation.TvShowWithCategory

class TvShowLocalMapper(
    private val categoryLocalMapper: CategoryLocalMapper
) {

    fun mapFromLocal(dto: TvShowWithCategory): TvShow {
        return TvShow(
            id = dto.tvShow.tvShowId,
            name = dto.tvShow.name,
            description = dto.tvShow.description,
            poster = dto.tvShow.poster,
            productionYear = dto.tvShow.productionYear,
            rating = dto.tvShow.rating,
            categories = categoryLocalMapper.mapListFromTvShowLocal(dto.categories),
            popularity = dto.tvShow.popularity
        )
    }

    fun mapToLocal(domain: TvShow): LocalTvShowDto {
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

    fun mapListFromLocal(dtos: List<TvShowWithCategory>): List<TvShow> {
        return dtos.map { mapFromLocal(it) }
    }

    fun mapListToLocal(domains: List<TvShow>): List<LocalTvShowDto> {
        return domains.map { mapToLocal(it) }
    }
}