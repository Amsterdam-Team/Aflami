package com.amsterdam.repository.mapper

import com.amsterdam.domain.utils.TvShowWatchHistory
import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TvShowWatchHistoryMapperTest {
    @Test
    fun `toEntity should map DTOs to TvShowWatchHistory entity`() {
        val result = tvShowWatchHistoryDto.toEntity(tvShowLocalDto)

        assertThat(result).isEqualTo(expectedWatchHistory)
    }

    private val tvShowLocalDto = TvShowLocalDto(
        tvShowId = 201L,
        storedLanguage = "en",
        name = "Test TV Show",
        description = "An overview.",
        poster = "/poster.jpg",
        airDate = LocalDate.parse("2023-10-26"),
        rating = 8.5f,
        popularity = 1500.0,
        seasonCount = 5,
        originCountry = "US"
    )

    private val tvShowWatchHistoryDto = TvShowWatchHistoryDto(
        tvShowId = 201L,
        watchedDate = Instant.parse("2024-01-20T12:00:00Z")
    )

    private val expectedWatchHistory = TvShowWatchHistory(
        tvShow = tvShowLocalDto.toEntity(),
        lastWatchedTime = tvShowWatchHistoryDto.watchedDate
    )
}