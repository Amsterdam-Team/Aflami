package com.amsterdam.repository.dto.remote

import com.amsterdam.repository.utils.ImageBaseUrlsConstant.BASE_IMAGE_URL_W500
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeRemoteDto(
    @SerialName("id") val id: Long,
    @SerialName("episode_number") val episodeNumber: Int,
    @SerialName("name") val title: String,
    @SerialName("runtime") val runtime: String?,
    @SerialName("air_date") val airDate: String?,
    @SerialName("overview") val overview: String,
    @SerialName("still_path") val stillPath: String?,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("season_number") val seasonNumber: Int
) {
    val fullStillPath: String?
        get() = stillPath?.let { BASE_IMAGE_URL_W500 + it }
}
