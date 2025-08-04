package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Season
import com.amsterdam.repository.dto.remote.SeasonDto

fun SeasonDto.toSeasonEntity(): Season {
    return Season(
        id = id,
        seasonNumber = seasonNumber,
        title = title,
        episodeCount = episodeCount
    )
}

fun List<SeasonDto>.toSeasonEntityList(): List<Season>  = map { it.toSeasonEntity() }