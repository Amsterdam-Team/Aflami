package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.mapper.remoteToLocal.testFactory.createRemoteMovieDetailsResponse
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test


class MovieDetailsMapperTest {

    @Test
    fun `should map RemoteMovieDetailsResponse to LocalMovieDto correctly`() {
        // Arrange
        val storedLanguage = "en"
        val remoteDto = createRemoteMovieDetailsResponse(
            id = 550L,
            title = "Fight Club",
            overview = "A ticking-time-bomb insomniac...",
            posterPath = "/poster.jpg",
            releaseDate = "1999-10-15",
            voteAverage = 8.4,
            popularity = 50.0,
            runtime = 139,
            originCountry = listOf("US", "DE")
        )

        // Act
        val localDto = remoteDto.toLocalDto(storedLanguage)

        // Assert
        assertThat(localDto.movieId).isEqualTo(550L)
        assertThat(localDto.storedLanguage).isEqualTo("en")
        assertThat(localDto.name).isEqualTo("Fight Club")
        assertThat(localDto.description).isEqualTo("A ticking-time-bomb insomniac...")
        assertThat(localDto.poster).isEqualTo("/poster.jpg")
        assertThat(localDto.releaseDate).isEqualTo(LocalDate(1999, 10, 15))
        assertThat(localDto.rating).isEqualTo(8.4f)
        assertThat(localDto.popularity).isEqualTo(50.0)
        assertThat(localDto.movieLength).isEqualTo(139)
        assertThat(localDto.originCountry).isEqualTo("US")
    }

    @Test
    fun `should handle empty posterPath and originCountry correctly`() {
        // Arrange
        val storedLanguage = "fr"
        val remoteDto = createRemoteMovieDetailsResponse(
            id = 123L,
            title = "Movie without details",
            overview = "Some brief overview.",
            posterPath = null,
            releaseDate = "2023-01-01",
            voteAverage = 0.0,
            popularity = 0.0,
            runtime = 0,
            originCountry = emptyList()
        )

        // Act
        val localDto = remoteDto.toLocalDto(storedLanguage)

        // Assert
        assertThat(localDto.poster).isEmpty()
        assertThat(localDto.originCountry).isEmpty()
    }
}