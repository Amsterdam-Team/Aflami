/*
package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MovieGenreLocalMapperTest {

    private lateinit var mapper: MovieGenreLocalMapper

    @BeforeEach
    fun setUp() {
        mapper = MovieGenreLocalMapper()
    }

    @Test
    fun `toEntity should return ALL genre when categoryId is invalid`() {
        // Arrange
        val dto = LocalMovieCategoryDto(
            categoryId = 100,
            name = "Unknown"
        )

        // Act
        val result = mapper.toEntity(dto)

        // Assert
        assertThat(result).isEqualTo(MovieGenre.ALL)
    }

    @Test
    fun `toEntity should return ALL genre when categoryId is 0`() {
        val dto = LocalMovieCategoryDto(
            categoryId = 0,
            name = "All"
        )

        val result = mapper.toEntity(dto)

        assertThat(result).isEqualTo(MovieGenre.ALL)
    }
}
*/
