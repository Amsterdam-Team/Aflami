package com.example.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteMovieSearchResponseDto(
    @SerialName("page")
    val page: Int,
    @SerialName("result")
    val movies: List<MovieSearchItem>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
) {
    @Serializable
    data class MovieSearchItem(
        @SerialName("id")
        val id: Long,
        @SerialName("title")
        val name: String,
        @SerialName("original_title")
        val originalTitle: String,
        @SerialName("poster_path")
        val posterPath: String,
        @SerialName("adult")
        val hasAdultContent: Boolean,
        @SerialName("vote_average")
        val rating: Double,
        @SerialName("vote_count")
        val voteCount: Int,
        @SerialName("overview")
        val description: String,
        @SerialName("release_date")
        val releaseDate: String,
        @SerialName("video")
        val video: Boolean,
        @SerialName("backdrop_path")
        val backdropPath: String,
        @SerialName("genre_ids")
        val genreIds: List<Int>,
        @SerialName("original_language")
        val originalLanguage: String,
        @SerialName("popularity")
        val popularity: Double
    )
}