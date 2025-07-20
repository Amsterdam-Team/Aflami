package com.example.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeDto(
    @SerialName("id") val id: Long,
    @SerialName("episode_number") val episodeNumber: Int,
    @SerialName("name") val title: String,
    @SerialName("runtime") val runtime: String?,
    @SerialName("air_date") val airDate: String?,
    @SerialName("overview") val overview: String,
    @SerialName("still_path") val stillPath: String? = null,
    @SerialName("vote_average") val voteAverage: String,
    @SerialName("season_number") val seasonNumber: Int,
)
