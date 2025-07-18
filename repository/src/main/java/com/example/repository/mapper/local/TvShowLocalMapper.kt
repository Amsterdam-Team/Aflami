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
            poster = dto.poster,
            productionYear = dto.productionYear,
            rating = dto.rating,
            categories = emptyList(),
            popularity = dto.popularity,
            seasonCount = dto.tvShow.seasonCount,
            originCountry = dto.tvShow.originCountry,
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
            popularity = domain.popularity,
            seasonCount = domain.seasonCount,
            originCountry = domain.originCountry,
        )
    }
}