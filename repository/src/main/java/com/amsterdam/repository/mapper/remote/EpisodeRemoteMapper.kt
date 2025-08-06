package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Episode
import com.amsterdam.repository.dto.remote.EpisodeDto
import com.amsterdam.repository.utils.toSafeLocalDate


fun EpisodeDto.toEntity(videoUrl:String): Episode {
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
        videoUrl = videoUrl
    )
}

fun List<EpisodeDto>.toEntityList(videoUrl:String): List<Episode> = map { it.toEntity(videoUrl =videoUrl) }