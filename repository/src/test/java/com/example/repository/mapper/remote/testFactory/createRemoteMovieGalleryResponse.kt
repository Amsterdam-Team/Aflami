package com.example.repository.mapper.remote.testFactory

import com.example.repository.dto.remote.movieGallery.GalleryImageDto
import com.example.repository.dto.remote.movieGallery.RemoteMovieGalleryResponse


fun createRemoteMovieGalleryResponse(
    id: Long = 123,
    backdrops: List<GalleryImageDto> = listOf(
        GalleryImageDto(
            aspectRatio = 1.78,
            height = 1080,
            language = null,
            filePath = "/img1.jpg",
            voteAverage = 8.5,
            voteCount = 1000,
            width = 1920
        ),
        GalleryImageDto(
            aspectRatio = 1.78,
            height = 720,
            language = "en",
            filePath = "/img2.jpg",
            voteAverage = 7.0,
            voteCount = 500,
            width = 1280
        )
    ),
    logos: List<GalleryImageDto> = emptyList(),
    posters: List<GalleryImageDto> = emptyList()
): RemoteMovieGalleryResponse {
    return RemoteMovieGalleryResponse(
        id = id,
        backdrops = backdrops,
        logos = logos,
        posters = posters
    )
}

