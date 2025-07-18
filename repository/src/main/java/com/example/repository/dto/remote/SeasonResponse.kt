package com.example.repository.dto.remote

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val title: String,
    @SerialName("air_date") val airDate: Instant,
    @SerialName("season_number") val seasonNumber: Int,
    @SerialName("episode_count") val episodeCount: Int,
    @SerialName("episodes") val episodes: List<EpisodeDto>,
)
