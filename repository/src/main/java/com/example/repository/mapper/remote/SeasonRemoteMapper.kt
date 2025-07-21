package com.example.repository.mapper.remote

import com.example.entity.Season
import com.example.repository.dto.remote.SeasonDto
import com.example.repository.mapper.shared.EntityMapper

class SeasonRemoteMapper : EntityMapper<SeasonDto, Season> {
    override fun toEntity(dto: SeasonDto): Season {
        return Season(
            id = dto.id,
            seasonNumber = dto.seasonNumber,
            title = dto.title,
            episodeCount = dto.episodeCount
        )
    }
}