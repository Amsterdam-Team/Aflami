package com.example.repository.dto.remote

import kotlinx.serialization.SerialName


data class EpisodeResponse(
    @SerialName("air_date")
    val airDate: String,
    @SerialName("episodes")
    val episodes: List<EpisodeDto>,
    @SerialName("name")
    val name: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("id")
    val id: Long,
    @SerialName("poster_path")
    val posterPath: String,
    @SerialName("season_number")
    val seasonNumber: Long,
    @SerialName("vote_average")
    val voteAverage: Double,
)
