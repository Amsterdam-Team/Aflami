package com.amsterdam.repository.dto.remote

import com.amsterdam.repository.utils.ImageBaseUrlsConstant.BASE_IMAGE_URL_W300
import com.amsterdam.repository.utils.ImageBaseUrlsConstant.BASE_IMAGE_URL_W500
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowItemRemoteDto(
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
    @SerialName("vote_count") val voteCount: Int,
    @SerialName("seasons") val seasons: List<SeasonRemoteDto> = emptyList(),
    @SerialName("number_of_seasons") val seasonCount: Int = 0,
    @SerialName("production_companies") val productionCompanies: List<ProductionCompanyRemoteDto> = emptyList(),
    @SerialName("rating") val rating: Float = 0f,
){
    val fullPosterPath: String?
        get() = posterPath?.let { BASE_IMAGE_URL_W500 + it }
    val fullBackdropPath: String?
        get() = backdropPath?.let { BASE_IMAGE_URL_W300 + it }
}