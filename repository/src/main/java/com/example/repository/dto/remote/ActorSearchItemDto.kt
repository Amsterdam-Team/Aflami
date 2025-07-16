package com.example.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorSearchItemDto(
    @SerialName("adult") val adult: Boolean,
    @SerialName("gender") val gender: Int,
    @SerialName("id") val id: Int,

    @SerialName("known_for") val knownFor: List<KnownFor>,
    @SerialName("known_for_department") val knownForDepartment: String? = null,
    @SerialName("name") val name: String,

    @SerialName("original_name") val originalName: String,
    @SerialName("popularity") val popularity: Double,
    @SerialName("profile_path") val profilePath: String? = null
) {
    @Serializable
    data class KnownFor(
        @SerialName("adult") val adult: Boolean,
        @SerialName("backdrop_path") val backdropPath: String? = null,
        @SerialName("genre_ids") val genreIds: List<Int>,

        @SerialName("id") val id: Int,
        @SerialName("media_type") val mediaType: String,
        @SerialName("original_language") val originalLanguage: String,

        @SerialName("original_title") val originalTitle: String? = null,
        @SerialName("title") val title: String? = null,
        @SerialName("release_date") val releaseDate: String? = null,

        @SerialName("original_name") val originalName: String? = null,
        @SerialName("name") val name: String? = null,
        @SerialName("first_air_date") val firstAirDate: String? = null,

        @SerialName("origin_country") val originCountry: List<String>? = null,
        @SerialName("overview") val overview: String,
        @SerialName("popularity") val popularity: Double,

        @SerialName("poster_path") val posterPath: String? = null,
        @SerialName("video") val video: Boolean? = null,
        @SerialName("vote_average") val voteAverage: Double,

        @SerialName("vote_count") val voteCount: Int
    )
}
