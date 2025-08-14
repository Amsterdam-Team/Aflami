package com.amsterdam.repository.mapper

import com.amsterdam.entity.Season
import com.amsterdam.repository.dto.remote.SeasonDto

fun SeasonDto.toEntity(): Season {
    return Season(
        id = id,
        seasonNumber = seasonNumber,
        title = title,
        episodeCount = episodeCount
    )
}

fun List<SeasonDto>.toEntityList(): List<Season>  = map { it.toEntity() }