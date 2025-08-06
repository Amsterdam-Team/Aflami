package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.mapper.remoteToLocal.testFactory.createRemoteMovieItemDto
import com.amsterdam.repository.utils.ImageBaseUrlsConstant.BASE_IMAGE_URL_W300
import com.amsterdam.repository.utils.ImageBaseUrlsConstant.BASE_IMAGE_URL_W500
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test


class RemoteToLocalMovieMappersTest {

    @Test
    fun `should map to LocalMovieDto with poster when isPoster is true`() {
        // Arrange
        val storedLanguage = "en"
        val remoteItem = createRemoteMovieItemDto(
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg"
        )
        val isPoster = true

        // Act
        val localDto = remoteItem.toLocalDto(isPoster, storedLanguage)

        // Assert
        assertThat(localDto.poster).isEqualTo(BASE_IMAGE_URL_W500 + "/poster.jpg")
        assertThat(localDto.movieId).isEqualTo(remoteItem.id)
        assertThat(localDto.name).isEqualTo(remoteItem.title)
    }

    @Test
    fun `should map to LocalMovieDto with backdrop when isPoster is false`() {
        // Arrange
        val storedLanguage = "en"
        val remoteItem = createRemoteMovieItemDto(
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg"
        )
        val isPoster = false

        // Act
        val localDto = remoteItem.toLocalDto(isPoster, storedLanguage)

        // Assert
        assertThat(localDto.poster).isEqualTo(BASE_IMAGE_URL_W300 + "/backdrop.jpg")
        assertThat(localDto.movieId).isEqualTo(remoteItem.id)
        assertThat(localDto.name).isEqualTo(remoteItem.title)
    }

    @Test
    fun `should handle null poster and backdrop paths correctly`() {
        // Arrange
        val storedLanguage = "en"
        val remoteItem = createRemoteMovieItemDto(
            posterPath = null,
            backdropPath = null
        )

        // Act
        val localDtoWithPosterFlag = remoteItem.toLocalDto(isPoster = true, storedLanguage)
        val localDtoWithBackdropFlag = remoteItem.toLocalDto(isPoster = false, storedLanguage)

        // Assert
        assertThat(localDtoWithPosterFlag.poster).isEmpty()
        assertThat(localDtoWithBackdropFlag.poster).isEmpty()
    }

    @Test
    fun `should map a list of items to a list of LocalMovieDto with posters`() {
        // Arrange
        val storedLanguage = "en"
        val remoteList = listOf(
            createRemoteMovieItemDto(id = 1, posterPath = "/poster1.jpg"),
            createRemoteMovieItemDto(id = 2, posterPath = "/poster2.jpg")
        )

        // Act
        val localList = remoteList.toLocalMovieDtoList(isPoster = true, storedLanguage)

        // Assert
        assertThat(localList).hasSize(2)
        assertThat(localList[0].poster).isEqualTo(BASE_IMAGE_URL_W500 + "/poster1.jpg")
        assertThat(localList[1].poster).isEqualTo(BASE_IMAGE_URL_W500 + "/poster2.jpg")
    }

    @Test
    fun `should map a list of items to a list of LocalMovieDto with backdrops`() {
        // Arrange
        val storedLanguage = "en"
        val remoteList = listOf(
            createRemoteMovieItemDto(id = 3, backdropPath = "/backdrop1.jpg"),
            createRemoteMovieItemDto(id = 4, backdropPath = "/backdrop2.jpg")
        )

        // Act
        val localList = remoteList.toLocalMovieDtoList(isPoster = false, storedLanguage)

        // Assert
        assertThat(localList).hasSize(2)
        assertThat(localList[0].poster).isEqualTo(BASE_IMAGE_URL_W300 + "/backdrop1.jpg")
        assertThat(localList[1].poster).isEqualTo(BASE_IMAGE_URL_W300 + "/backdrop2.jpg")
    }
}