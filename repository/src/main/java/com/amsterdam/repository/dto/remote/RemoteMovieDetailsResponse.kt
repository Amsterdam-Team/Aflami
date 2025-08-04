package com.amsterdam.repository.dto.remote

import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.dto.remote.review.ReviewsResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteMovieDetailsResponse(
    @SerialName("adult") val adult: Boolean,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerialName("id") val id: Long,
    @SerialName("original_language") val originalLanguage: String,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("overview") val overview: String,
    @SerialName("popularity") val popularity: Double,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("production_companies") val productionCompanies: List<ProductionCompanyDto> = emptyList(),
    @SerialName("release_date") val releaseDate: String,
    @SerialName("title") val title: String,
    @SerialName("video") val video: Boolean,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int,
    @SerialName("origin_country") val originCountry: List<String> = emptyList(),
    @SerialName("runtime") val runtime: Int = 0,
    @SerialName("genres") val genres: List<RemoteCategoryDto> = emptyList(),
    @SerialName("reviews") val reviews: ReviewsResponse,
    @SerialName("credits") val credits: RemoteCastAndCrewResponse,
    @SerialName("similar") val similar: RemoteMovieResponse,
    @SerialName("images") val images: RemoteGalleryResponse,
    @SerialName("videos") val videos: VideoResponse
)