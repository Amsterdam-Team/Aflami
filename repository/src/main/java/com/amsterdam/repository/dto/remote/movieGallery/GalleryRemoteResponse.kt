package com.amsterdam.repository.dto.remote.movieGallery

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GalleryRemoteResponse(
    @SerialName("id") val id: Long? = null,
    @SerialName("backdrops")  val backdrops: List<GalleryImageRemoteDto>,
    @SerialName("logos")  val logos: List<GalleryImageRemoteDto>,
    @SerialName("posters") val posters: List<GalleryImageRemoteDto>
)