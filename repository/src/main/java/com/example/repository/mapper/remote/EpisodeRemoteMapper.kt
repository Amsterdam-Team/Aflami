package com.example.repository.mapper.remote

import android.util.Log
import com.example.entity.Episode
import com.example.repository.dto.remote.EpisodeDto
import com.example.repository.mapper.shared.EntityMapper
import kotlinx.datetime.toLocalDate

class EpisodeRemoteMapper : EntityMapper<EpisodeDto, Episode> {
    override fun toEntity(dto: EpisodeDto): Episode {
        Log.d("EpisodeRemoteMapper", "Mapping EpisodeDto to Episode")
        return Episode(
            id = dto.id,
            title = dto.title,
            episodeNumber = dto.episodeNumber,
            description = dto.overview,
            stillUrl = dto.stillPath ?: "",
            rating = dto.voteAverage.toFloat(),
            airDate = dto.airDate?.toLocalDate(),
            seasonNumber = dto.seasonNumber,
            runtime = dto.runtime?.toInt() ?: 0,
        )
    }
}