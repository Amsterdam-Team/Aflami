package com.amsterdam.repository.mapper.remote

import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MovieRemoteMapperTest {
    @Test
    fun `given valid RemoteMovieItemDto when mapped then return correct Movie entity`() {
        // Given
        val dto = RemoteMovieItemDto(
            id = 123,
            title = "Test Movie",
            overview = "This is a test movie",
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            releaseDate = "2023-10-01",
            voteAverage = 7.8,
            popularity = 123.4,
            genreIds = listOf(1, 2),
            originCountry = listOf("US"),
            runtime = 110,
            adult = false,
            originalLanguage = "en",
            originalTitle = "Original Title",
            video = false,
            voteCount = 1000
        )

        // When
        val result = dto.toEntity(isPoster = true, videoUrl = "https://video.com")

        // Then
        assertThat(result.id).isEqualTo(123)
        assertThat(result.name).isEqualTo("Test Movie")
        assertThat(result.description).isEqualTo("This is a test movie")
        assertThat(result.posterUrl).isEqualTo(dto.fullPosterUrl.orEmpty())
        assertThat(result.releaseDate.toString()).isEqualTo("2023-10-01")
        assertThat(result.categories).hasSize(2)
        assertThat(result.rating).isEqualTo(7.8f)
        assertThat(result.popularity).isEqualTo(123.4)
        assertThat(result.originCountry).isEqualTo("US")
        assertThat(result.runTimeInMinutes).isEqualTo(110)
        assertThat(result.videoUrl).isEqualTo("https://video.com")
    }
    @Test
    fun `given list of RemoteMovieItemDto when mapped then return list of Movie entities`() {
        // Given
        val dtoList = listOf(
            RemoteMovieItemDto(id = 1, title = "Movie 1", genreIds = listOf(1), releaseDate = "2022-01-01", voteAverage = 8.0, popularity = 100.0, overview = "", posterPath = "", backdropPath = "", originCountry = listOf(), runtime = 90, productionCompanies = emptyList(), adult = false, originalLanguage = "en", originalTitle = "", video = false, voteCount = 0, genres = emptyList()),
            RemoteMovieItemDto(id = 2, title = "Movie 2", genreIds = listOf(2), releaseDate = "2022-02-02", voteAverage = 7.5, popularity = 200.0, overview = "", posterPath = "", backdropPath = "", originCountry = listOf(), runtime = 100, productionCompanies = emptyList(), adult = false, originalLanguage = "en", originalTitle = "", video = false, voteCount = 0, genres = emptyList())
        )

        // When
        val result = dtoList.toMovieEntityList(isPoster = false)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(1)
        assertThat(result[1].id).isEqualTo(2)
    }


}

