package com.example.repository.dto.remote.movieGallery

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GalleryImageDto(
    @SerialName("aspect_ratio") val aspectRatio: Double,
    @SerialName("height") val height: Int,
    @SerialName("iso_639_1") val language: String? = null,
    @SerialName("file_path") val filePath: String,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int,
    @SerialName("width") val width: Int
){
    val fullFilePath: String?
        get() = filePath.let { BuildConfig.BASE_IMAGE_URL + it }
}