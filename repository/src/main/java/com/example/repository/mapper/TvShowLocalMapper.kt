package com.example.repository.mapper

import com.example.entity.Category
import com.example.entity.TvShow
import com.example.repository.dto.local.LocalTvShowDto

class TvShowLocalMapper {

    fun mapFromLocal(dto: LocalTvShowDto, categories: List<Category> = emptyList()): TvShow {
        return TvShow(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            poster = dto.poster,
            productionYear = dto.productionYear,
            rating = dto.rating,
            categories = categories
        )
    }

    fun mapToLocal(domain: TvShow): LocalTvShowDto {
        return LocalTvShowDto(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            poster = domain.poster,
            productionYear = domain.productionYear,
            rating = domain.rating
        )
    }

    fun mapListFromLocal(dtos: List<LocalTvShowDto>, categoriesMap: Map<Long, List<Category>>): List<TvShow> {
        return dtos.map { dto ->
            val categories = categoriesMap[dto.id] ?: emptyList()
            mapFromLocal(dto, categories)
        }
    }

    fun mapListToLocal(domains: List<TvShow>): List<LocalTvShowDto> {
        return domains.map { mapToLocal(it) }
    }
}