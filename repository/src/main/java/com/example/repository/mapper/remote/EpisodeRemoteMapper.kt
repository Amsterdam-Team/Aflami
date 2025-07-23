package com.example.repository.mapper.remote

import com.example.entity.Episode
import com.example.repository.dto.remote.EpisodeDto
import com.example.repository.mapper.shared.EntityMapper
import kotlinx.datetime.toLocalDate

class EpisodeRemoteMapper : EntityMapper<EpisodeDto, Episode> {
    override fun toEntity(dto: EpisodeDto): Episode {
        return Episode(
            id = dto.id,
            title = dto.title,
            episodeNumber = dto.episodeNumber,
            description = dto.overview,
            episodeImageUrl = dto.fullStillPath.orEmpty(),
            rating = dto.voteAverage.toFloat(),
            airDate = dto.airDate?.toLocalDate(),
            seasonNumber = dto.seasonNumber,
            runtime = dto.runtime?.toInt() ?: 0,
        )
    }
}