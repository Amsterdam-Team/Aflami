package com.example.remotedatasource.datasource.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorSearchResponseDto(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val actors: List<ActorSearchItem>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
) {
    @Serializable
    data class KnownFor(
        @SerialName("adult")
        val adult: Boolean,
        @SerialName("backdrop_path")
        val backdropPath: String,
        @SerialName("genre_ids")
        val genreIds: List<Int>,
        @SerialName("id")
        val id: Int,
        @SerialName("media_type")
        val mediaType: String,
        @SerialName("original_language")
        val originalLanguage: String,
        @SerialName("original_title")
        val originalTitle: String,
        @SerialName("overview")
        val overview: String,
        @SerialName("popularity")
        val popularity: Double,
        @SerialName("poster_path")
        val posterPath: String,
        @SerialName("release_date")
        val releaseDate: String,
        @SerialName("title")
        val title: String,
        @SerialName("video")
        val video: Boolean,
        @SerialName("vote_average")
        val voteAverage: Double,
        @SerialName("vote_count")
        val voteCount: Int
    )

    @Serializable
    data class ActorSearchItem(
        @SerialName("adult")
        val adult: Boolean,
        @SerialName("gender")
        val gender: Int,
        @SerialName("id")
        val id: Int,
        @SerialName("known_for")
        val knownFor: List<KnownFor>,
        @SerialName("known_for_department")
        val knownForDepartment: String,
        @SerialName("name")
        val name: String,
        @SerialName("original_name")
        val originalName: String,
        @SerialName("popularity")
        val popularity: Double,
        @SerialName("profile_path")
        val profilePath: String
    )
}