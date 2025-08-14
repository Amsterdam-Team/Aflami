package com.amsterdam.repository.mapper

import com.amsterdam.entity.Episode
import com.amsterdam.repository.dto.remote.EpisodeRemoteDto
import com.amsterdam.repository.utils.toSafeLocalDate


fun EpisodeRemoteDto.toEntity(): Episode {
    return Episode(
        id = id,
        title = title,
        episodeNumber = episodeNumber,
        description = overview,
        episodeImageUrl = fullStillPath.orEmpty(),
        rating = voteAverage.toFloat(),
        airDate = airDate?.toSafeLocalDate(),
        seasonNumber = seasonNumber,
        runTimeInMinutes = runtime?.toInt() ?: 0,
        videoUrl = ""
    )
}

fun List<EpisodeRemoteDto>.toEntityList(): List<Episode> = map { it.toEntity() }