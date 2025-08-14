package com.amsterdam.repository.dto.remote

import com.amsterdam.repository.utils.ImageBaseUrlsConstant.BASE_IMAGE_URL_W300
import com.amsterdam.repository.utils.ImageBaseUrlsConstant.BASE_IMAGE_URL_W500
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserListItemRemoteDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("overview") val overview: String,
    @SerialName("original_language") val originalLanguage: String,
    @SerialName("genre_ids") val genreIds: List<Int>? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("media_type") val mediaType: String,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("popularity") val popularity: Double? = null,
    @SerialName("vote_average") val voteAverage: Double? = null,
    @SerialName("adult") val adult: Boolean,
    @SerialName("vote_count") val voteCount: Int? = null
) {
    val fullPosterUrl: String?
        get() = posterPath?.let { BASE_IMAGE_URL_W500 + it }

    val fullBackdropUrl: String?
        get() = backdropPath?.let { BASE_IMAGE_URL_W300 + it }
}