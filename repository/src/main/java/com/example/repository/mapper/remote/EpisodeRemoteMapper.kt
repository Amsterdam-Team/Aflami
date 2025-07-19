package com.example.repository.mapper.remote

import com.example.entity.Episode
import com.example.repository.dto.remote.EpisodeDto
import com.example.repository.mapper.shared.EntityMapper
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class EpisodeRemoteMapper : EntityMapper<EpisodeDto, Episode> {
    override fun toEntity(dto: EpisodeDto): Episode {
        return Episode(
            id = dto.id,
            title = dto.title,
            episodeNumber = dto.episodeNumber,
            description = dto.overview,
            stillUrl = dto.stillPath,
            rating = dto.voteAverage.toFloat(),
            airDate = dto.airDate.toLocalDateTime(TimeZone.UTC).date,
            seasonNumber = dto.seasonNumber,
            runtime = dto.runtime.toInt(),
        )
    }
}