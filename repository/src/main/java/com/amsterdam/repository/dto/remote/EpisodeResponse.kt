package com.amsterdam.repository.dto.remote

import com.amsterdam.repository.utils.ImageBaseUrlsConstant.BASE_IMAGE_URL_W500
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeResponse(
    @SerialName("air_date") val airDate: String?,
    @SerialName("episodes") val episodes: List<EpisodeDto>,
    @SerialName("name") val name: String,
    @SerialName("overview") val overview: String,
    @SerialName("id") val id: Long,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("season_number") val seasonNumber: Long,
    @SerialName("vote_average") val voteAverage: Double,
) {
    val fullPosterPath: String?
        get() = posterPath?.let { BASE_IMAGE_URL_W500 + it }
}