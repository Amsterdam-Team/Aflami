package com.example.repository.dto.remote.movieGallery

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GalleryImageDto(
    @SerialName("aspect_ratio") val aspectRatio: Double,
    val height: Int,
    @SerialName("iso_639_1") val language: String? = null,
    @SerialName("file_path") val filePath: String,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int,
    val width: Int
)