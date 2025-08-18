package com.amsterdam.repository.mapper

import com.amsterdam.domain.utils.MovieWatchHistory
import com.amsterdam.repository.dto.local.MovieLocalDto
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MovieWatchHistoryMapperTest {
    @Nested
    inner class ToEntityTest {
        @Test
        fun `toEntity should map DTOs to MovieWatchHistory entity`() {
            val expectedMovieEntity = movieLocalDto.toEntity()

            val result = movieWatchHistoryDto.toEntity(movieLocalDto)

            assertThat(result).isEqualTo(
                MovieWatchHistory(
                    movie = expectedMovieEntity,
                    lastWatchedTime = movieWatchHistoryDto.watchedDate
                )
            )
        }
    }

    private val movieLocalDto = MovieLocalDto(
        movieId = 101L,
        storedLanguage = "en",
        name = "Test Movie",
        description = "An overview.",
        poster = "/poster.jpg",
        releaseDate = LocalDate.parse("2023-10-26"),
        rating = 8.5f,
        popularity = 1500.0,
        movieLength = 120,
        originCountry = "US",
        isAdult = false
    )

    private val movieWatchHistoryDto = MovieWatchHistoryDto(
        movieId = 101L,
        watchedDate = Instant.parse("2024-01-15T10:30:00Z")
    )
}