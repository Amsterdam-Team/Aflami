package com.amsterdam.repository.mapper

import com.amsterdam.entity.Season
import com.amsterdam.repository.dto.remote.SeasonRemoteDto

fun SeasonRemoteDto.toEntity(): Season {
    return Season(
        id = id,
        seasonNumber = seasonNumber,
        title = title,
        episodeCount = episodeCount
    )
}

fun List<SeasonRemoteDto>.toEntityList(): List<Season> = map { it.toEntity() }