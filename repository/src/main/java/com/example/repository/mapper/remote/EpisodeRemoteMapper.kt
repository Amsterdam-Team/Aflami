package com.example.repository.mapper.remote

import com.example.entity.Episode
import com.example.repository.dto.remote.EpisodeDto
import com.example.repository.dto.remote.SeasonResponse
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class EpisodeRemoteMapper {
    fun mapToEpisodes(seasonResponse: SeasonResponse): List<Episode> {
        return seasonResponse.episodes.map { mapToEpisode(it) }
    }

    fun mapToEpisode(episodeDto: EpisodeDto): Episode {
        return Episode(
            id = episodeDto.id,
            title = episodeDto.title,
            episodeNumber = episodeDto.episodeNumber,
            description = episodeDto.overview,
            stillUrl = episodeDto.stillPath,
            rating = episodeDto.voteAverage.toFloat(),
            airDate = episodeDto.airDate.toLocalDateTime(TimeZone.UTC).date,
            seasonNumber = episodeDto.seasonNumber,
            runtime = episodeDto.runtime.toInt(),
        )
    }
}