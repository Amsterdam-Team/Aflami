//package com.example.repository.mapper.remote
//
//import com.example.repository.BuildConfig
//import com.example.repository.dto.remote.movieGallery.GalleryImageDto
//import com.example.repository.mapper.remote.testFactory.createRemoteMovieGalleryResponse
//import com.google.common.truth.Truth.assertThat
//import org.junit.jupiter.api.Test
//
//class PostersRemoteMapperTest {
//
//    private val mapper = PostersRemoteMapper()
//
//    @Test
//    fun `toEntity should map poster file paths to full image URLs`() {
//        val posters = listOf(
//            GalleryImageDto(
//                aspectRatio = 1.0,
//                height = 100,
//                language = "en",
//                filePath = "/poster1.jpg",
//                voteAverage = 9.0,
//                voteCount = 500,
//                width = 100
//            ),
//            GalleryImageDto(
//                aspectRatio = 1.5,
//                height = 200,
//                language = null,
//                filePath = "/poster2.jpg",
//                voteAverage = 8.0,
//                voteCount = 300,
//                width = 150
//            )
//        )
//
//        val dto = createRemoteMovieGalleryResponse(posters = posters)
//
//        val result = mapper.toEntity(dto)
//
//        assertThat(result).containsExactly(
//            BuildConfig.BASE_IMAGE_URL + "/poster1.jpg",
//            BuildConfig.BASE_IMAGE_URL + "/poster2.jpg"
//        )
//    }
//
//    @Test
//    fun `toEntity should return empty list when posters is empty`() {
//        val dto = createRemoteMovieGalleryResponse(posters = emptyList())
//
//        val result = mapper.toEntity(dto)
//
//        assertThat(result).isEmpty()
//    }
//}
