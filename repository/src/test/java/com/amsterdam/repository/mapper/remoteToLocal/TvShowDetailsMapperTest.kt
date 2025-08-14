package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.mapper.remoteToLocal.testFactory.createTvShowDetailsRemoteResponse
import com.amsterdam.repository.mapper.toLocalDto
import com.amsterdam.repository.utils.ImageBaseUrlsConstant.BASE_IMAGE_URL_W500
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TvShowDetailsMapperTest {

    @Test
    fun `should map TvShowDetailsRemoteResponse to LocalTvShowDto correctly`() {
        // Arrange
        val storedLanguage = "en"
        val posterPath = "/poster.jpg"
        val remoteDto = createTvShowDetailsRemoteResponse(
            id = 1399L,
            title = "Game of Thrones",
            overview = "Seven noble families fight for control of the mythical land of Westeros.",
            posterPath = posterPath,
            releaseDate = "2011-04-17",
            voteAverage = 8.4,
            popularity = 300.0,
            seasonCount = 8,
            originCountry = listOf("US")
        )

        // Act
        val localDto = remoteDto.toLocalDto(storedLanguage)

        // Assert
        assertThat(localDto.tvShowId).isEqualTo(1399L)
        assertThat(localDto.storedLanguage).isEqualTo("en")
        assertThat(localDto.name).isEqualTo("Game of Thrones")
        assertThat(localDto.description).isEqualTo("Seven noble families fight for control of the mythical land of Westeros.")
        assertThat(localDto.poster).isEqualTo(BASE_IMAGE_URL_W500 + posterPath)
        assertThat(localDto.airDate).isEqualTo(LocalDate(2011, 4, 17))
        assertThat(localDto.rating).isEqualTo(8.4f)
        assertThat(localDto.popularity).isEqualTo(300.0)
        assertThat(localDto.seasonCount).isEqualTo(8)
        assertThat(localDto.originCountry).isEqualTo("US")
    }

    @Test
    fun `should handle null poster and empty originCountry correctly`() {
        // Arrange
        val storedLanguage = "fr"
        val remoteDto = createTvShowDetailsRemoteResponse(
            id = 100L,
            title = "A Show",
            overview = "Some overview.",
            posterPath = null,
            releaseDate = "2023-01-01",
            voteAverage = 0.0,
            popularity = 0.0,
            seasonCount = 1,
            originCountry = emptyList()
        )

        // Act
        val localDto = remoteDto.toLocalDto(storedLanguage)

        // Assert
        assertThat(localDto.poster).isEmpty()
        assertThat(localDto.originCountry).isEmpty()
    }
}