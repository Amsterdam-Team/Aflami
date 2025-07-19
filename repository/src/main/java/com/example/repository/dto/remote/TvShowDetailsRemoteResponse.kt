package com.example.repository.dto.remote

import com.example.repository.BuildConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowDetailsRemoteResponse(
    @SerialName("adult") val adult: Boolean,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("genres") val genres: List<GenreDto>,
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
    @SerialName("seasons") val seasons: List<SeasonResponse> = emptyList(),
    @SerialName("number_of_seasons") val seasonCount: Int = 0,
){
    val fullPosterPath: String?
        get() = posterPath?.let { BuildConfig.BASE_IMAGE_URL + it }
}
