package com.example.repository.mapper.local

import com.example.entity.TvShow
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.relation.TvShowWithCategories

class TvShowLocalMapper(
    private val categoryLocalMapper: CategoryLocalMapper
) {

    fun mapFromLocal(dto: TvShowWithCategories): TvShow {
        return TvShow(
            id = dto.tvShow.tvId,
            name = dto.tvShow.name,
            description = dto.tvShow.description,
            poster = dto.tvShow.poster,
            productionYear = dto.tvShow.productionYear,
            rating = dto.tvShow.rating,
            categories = categoryLocalMapper.mapListFromLocal(dto.categories)
        )
    }

    fun mapToLocal(domain: TvShow): LocalTvShowDto {
        return LocalTvShowDto(
            tvId = domain.id,
            name = domain.name,
            description = domain.description,
            poster = domain.poster,
            productionYear = domain.productionYear,
            rating = domain.rating
        )
    }

    fun mapListFromLocal(dtoList: List<TvShowWithCategories>): List<TvShow> {
        return dtoList.map { mapFromLocal(it) }
    }

    fun mapListToLocal(domains: List<TvShow>): List<LocalTvShowDto> {
        return domains.map { mapToLocal(it) }
    }
}