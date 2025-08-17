package com.amsterdam.repository.mapper

import com.amsterdam.repository.dto.remote.movieGallery.GalleryImageRemoteDto
import com.amsterdam.repository.dto.remote.movieGallery.GalleryRemoteResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class GalleryMapperTest {
    @Test
    fun `toEntityList should map ONLY backdrops to a list of full image paths`() {
        val result = fullGalleryResponse.toEntityList()

        assertThat(result).isEqualTo(expectedImagePaths)
    }

    @Test
    fun `toEntityList should return an empty list when backdrops list is empty`() {
        val result = responseWithNoBackdrops.toEntityList()

        assertThat(result).isEmpty()
    }

    private val fullGalleryResponse = GalleryRemoteResponse(
        id = 1L,
        backdrops = listOf(
            createDummyImageDto("/backdrop1.jpg"),
            createDummyImageDto("/backdrop2.png")
        ),
        logos = listOf(createDummyImageDto("/logo1.svg")),
        posters = listOf(createDummyImageDto("/poster1.jpg"))
    )

    private val responseWithNoBackdrops = GalleryRemoteResponse(
        id = 3L,
        backdrops = emptyList(),
        logos = listOf(createDummyImageDto("/logo1.svg")),
        posters = listOf(createDummyImageDto("/poster1.jpg"))
    )

    private val expectedImagePaths = listOf(
        "https://image.tmdb.org/t/p/w500/backdrop1.jpg",
        "https://image.tmdb.org/t/p/w500/backdrop2.png"
    )

    private fun createDummyImageDto(path: String) = GalleryImageRemoteDto(
        filePath = path,
        aspectRatio = 1.778,
        height = 1080,
        language = "en",
        voteAverage = 5.0,
        voteCount = 100,
        width = 1920
    )
}