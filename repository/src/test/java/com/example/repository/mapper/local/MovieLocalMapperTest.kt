package com.example.repository.mapper.local

import com.example.repository.mapper.local.testFactory.createLocalMovieDtotest
import com.example.repository.mapper.local.testFactory.createMovie
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MovieLocalMapperTest {

    private lateinit var mapper: MovieLocalMapper

    @BeforeEach
    fun setUp() {
        mapper = MovieLocalMapper()
    }

    @Test
    @DisplayName("should return Movie entity when converting from LocalMovieDto")
    fun `toEntity should return Movie when given LocalMovieDto`() {
        // Arrange
        val dto = createLocalMovieDtotest()
        val expected = createMovie(
            id = 101,
            name = "Inception",
            description = "A mind-bending thriller",
            posterUrl = "poster_url.jpg",
            productionYear = 2010u,
            rating = 8.8f,
            popularity = 99.5,
            runTime = 148,
            originCountry = "USA",
            hasVideo = true,
            categories = emptyList()
        )

        // Act
        val result = mapper.toEntity(dto)

        // Assert
        assertThat(result).isEqualTo(expected)
    }

    @Test
    @DisplayName("should return LocalMovieDto when converting from Movie entity")
    fun `toDto should return LocalMovieDto when given Movie`() {
        // Arrange
        val entity = createMovie()
        val expected = createLocalMovieDtotest(
            movieId = 202,
            name = "Interstellar",
            description = "Exploration beyond stars",
            poster = "interstellar.jpg",
            productionYear = 2014,
            rating = 9.0f,
            popularity = 95.2,
            movieLength = 169,
            originCountry = "USA",
            hasVideo = false
        )

        // Act
        val result = mapper.toDto(entity)

        // Assert
        assertThat(result).isEqualTo(expected)
    }
}
