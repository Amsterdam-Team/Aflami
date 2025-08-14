package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.mapper.toEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TvShowLocalMapperTest {

    @Test
    fun `toEntity should map LocalTvShowDto to TvShow correctly`() {
        val dto = TvShowLocalDto(
            tvShowId = 1,
            name = "Game of Thrones",
            description = "A fantasy drama series",
            poster = "https://example.com/poster.jpg",
            storedLanguage = "en",
            airDate = LocalDate.parse(""),
            rating = 4.6f,
            popularity = 5.0,
            seasonCount = 4,
            originCountry = "",
        )

        val expected = Movie(
            id = 101,
            name = "Inception",
            description = "A mind-bending thriller",
            posterUrl = "poster_url.jpg",
            rating = 8.8f,
            popularity = 99.5,
            runTimeInMinutes = 148,
            originCountry = "USA",
            releaseDate = LocalDate.parse("2020-01-01"),
            categories = emptyList(),
            videoUrl = "",
        )

        val result = dto.toEntity()

        assertThat(result).isEqualTo(expected)
    }

}

