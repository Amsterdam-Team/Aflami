
package com.amsterdam.repository.mapper.local


import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MovieLocalMapperTest {


    @Test
    @DisplayName("should return Movie entity when converting from LocalMovieDto")
    fun `toEntity should return Movie when given LocalMovieDto`() {
        // Given
        val dto = LocalMovieDto(
            movieId = 101,
            name = "Inception",
            description = "A mind-bending thriller",
            poster = "poster_url.jpg",
            releaseDate = LocalDate.parse("2020-01-01"),
            rating = 8.8f,
            popularity = 99.5,
            movieLength = 148,
            originCountry = "USA",
            storedLanguage = "en",
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

        // When
        val result = dto.toEntity()

        // Then
        assertThat(result).isEqualTo(expected)
    }

}

