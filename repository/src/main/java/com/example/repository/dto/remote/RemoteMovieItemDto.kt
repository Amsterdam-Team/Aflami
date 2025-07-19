package com.example.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteMovieItemDto(
    @SerialName("adult") val adult: Boolean,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerialName("id") val id: Long,
    @SerialName("original_language") val originalLanguage: String,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("overview") val overview: String,
    @SerialName("popularity") val popularity: Double,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("title") val title: String,
    @SerialName("video") val video: Boolean,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int,
    @SerialName("origin_country") val originCountry: List<String> = emptyList(),
    @SerialName("runtime") val runtime: Int = 0,
    @SerialName("genres") val genres: List<GenreDto> = emptyList()
){
    val fullPosterUrl: String?
        get() = posterPath?.let { BuildConfig.BASE_IMAGE_URL + it }

    val fullBackdropUrl: String?
        get() = backdropPath?.let { BuildConfig.BASE_IMAGE_URL + it }
}
