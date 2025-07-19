package com.example.repository.mapper.remote

import com.example.entity.Season
import com.example.repository.dto.remote.SeasonResponse
import com.example.repository.mapper.shared.EntityMapper

class SeasonRemoteMapper : EntityMapper<SeasonResponse, Season> {
    override fun toEntity(dto: SeasonResponse): Season {
        return Season(
            id = dto.id,
            seasonNumber = dto.seasonNumber,
            episodeCount = dto.episodeCount
        )
    }
}