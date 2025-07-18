package com.example.repository.mapper.remote

import com.example.entity.Season
import com.example.repository.dto.remote.RemoteTvShowItemDto
import com.example.repository.dto.remote.SeasonResponse

class SeasonRemoteMapper {
    fun mapToSeasons(tvShowDto: RemoteTvShowItemDto): List<Season> {
        return tvShowDto.seasons.map { mapToSeason(it) }
    }

    fun mapToSeason(seasonResponse: SeasonResponse): Season {
        return Season(
            id = seasonResponse.id,
            seasonNumber = seasonResponse.seasonNumber,
            episodeCount = seasonResponse.episodeCount,
            episodes = seasonResponse.episodes.map { it.id }
        )
    }
}