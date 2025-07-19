package com.example.repository.mapper.local

import com.example.entity.TvShow
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.mapper.shared.DtoMapper
import com.example.repository.mapper.shared.EntityMapper

class TvShowLocalMapper : EntityMapper<LocalTvShowDto, TvShow>, DtoMapper<TvShow, LocalTvShowDto> {
    override fun toEntity(dto: LocalTvShowDto): TvShow {
        return TvShow(
            id = dto.tvShowId,
            name = dto.name,
            description = dto.description,
            posterUrl = dto.poster,
            productionYear = dto.productionYear.toUInt(),
            rating = dto.rating,
            categories = emptyList(),
            popularity = dto.popularity
        )
    }

    override fun toDto(entity: TvShow): LocalTvShowDto {
        return LocalTvShowDto(
            tvShowId = entity.id,
            name = entity.name,
            description = entity.description,
            poster = entity.posterUrl,
            productionYear = entity.productionYear.toInt(),
            rating = entity.rating,
            popularity = entity.popularity
        )
    }
}