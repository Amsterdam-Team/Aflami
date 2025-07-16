package com.example.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteTvShowItemDto(
    @SerialName("adult") val adult: Boolean,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("genre_ids") val genreIds: List<Int>,

    @SerialName("id") val id: Long,
    @SerialName("origin_country") val originCountry: List<String>,
    @SerialName("original_language") val originalLanguage: String,

    @SerialName("original_name") val originalTitle: String,

    @SerialName("overview") val overview: String,
    @SerialName("popularity") val popularity: Double,
    @SerialName("poster_path") val posterPath: String?,

    @SerialName("first_air_date") val releaseDate: String,
    @SerialName("name") val title: String,
    @SerialName("vote_average") val voteAverage: Double,

    @SerialName("vote_count") val voteCount: Int
)