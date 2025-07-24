//package com.example.repository.mapper.remote
//
//import com.example.repository.BuildConfig
//import com.example.repository.mapper.remote.testFactory.createRemoteMovieGalleryResponse
//import com.google.common.truth.Truth.assertThat
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//
//class GalleryRemoteMapperTest {
//
//    private lateinit var mapper: GalleryRemoteMapper
//
//    @BeforeEach
//    fun setUp() {
//        mapper = GalleryRemoteMapper()
//    }
//
//    @Test
//    fun `should map backdrops to list of full image URLs`() {
//        // Arrange
//        val response = createRemoteMovieGalleryResponse()
//
//        // Act
//        val result = mapper.toEntity(response)
//
//        // Assert
//        val expectedUrls = listOf(
//            BuildConfig.BASE_IMAGE_URL + "/img1.jpg",
//            BuildConfig.BASE_IMAGE_URL + "/img2.jpg"
//        )
//        assertThat(result).isEqualTo(expectedUrls)
//    }
//
//    @Test
//    fun `should return empty list when backdrops are empty`() {
//        // Arrange
//        val response = createRemoteMovieGalleryResponse(backdrops = emptyList())
//
//        // Act
//        val result = mapper.toEntity(response)
//
//        // Assert
//        assertThat(result).isEmpty()
//    }
//}
