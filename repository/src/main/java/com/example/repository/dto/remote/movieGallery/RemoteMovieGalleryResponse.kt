package com.example.repository.dto.remote.movieGallery

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteMovieGalleryResponse(
    @SerialName("id")  val id: Long,
    @SerialName("backdrops")  val backdrops: List<GalleryImageDto>,
    @SerialName("logos")  val logos: List<GalleryImageDto>,
    @SerialName("posters") val posters: List<GalleryImageDto>
)