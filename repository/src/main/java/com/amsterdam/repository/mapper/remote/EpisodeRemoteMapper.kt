package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Episode
import com.amsterdam.repository.dto.remote.EpisodeDto
import com.amsterdam.repository.mapper.shared.EntityMapper
import com.amsterdam.repository.utils.toSafeLocalDate

class EpisodeRemoteMapper : EntityMapper<EpisodeDto, Episode> {
    override fun toEntity(dto: EpisodeDto): Episode {
        return Episode(
            id = dto.id,
            title = dto.title,
            episodeNumber = dto.episodeNumber,
            description = dto.overview,
            episodeImageUrl = dto.fullStillPath.orEmpty(),
            rating = dto.voteAverage.toFloat(),
            airDate = dto.airDate?.toSafeLocalDate(),
            seasonNumber = dto.seasonNumber,
            runTimeInMinutes = dto.runtime?.toInt() ?: 0,
        )
    }
}