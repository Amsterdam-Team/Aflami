package com.example.repository.dto.remote.movieGallery

import kotlinx.serialization.Serializable

@Serializable
data class RemoteMovieGalleryResponse(
    val id: Long,
    val backdrops: List<GalleryImageDto>,
    val logos: List<GalleryImageDto>,
    val posters: List<GalleryImageDto>
)