/*
package com.example.repository.mapper.local

import com.example.entity.Movie
import com.example.entity.category.MovieGenre
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.mapper.local.testFactory.createLocalMovieDtotest
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MovieWithCategoriesLocalMapperTest {

    private lateinit var movieGenreLocalMapper: MovieGenreLocalMapper
    private lateinit var mapper: MovieWithCategoriesLocalMapper

    @BeforeEach
    fun setUp() {
        movieGenreLocalMapper = mockk()
        mapper = MovieWithCategoriesLocalMapper(movieGenreLocalMapper)
    }

    @Test
    @DisplayName("should return Movie when converting from MovieWithCategories")
    fun `toEntity should return Movie when given MovieWithCategories`() {
        // Arrange
        val localMovie = createLocalMovieDtotest(
            movieId = 10L,
            name = "The Matrix",
            description = "Sci-fi action",
            poster = "matrix.jpg",
            productionYear = 1999,
            rating = 8.7f,
            popularity = 80.0,
            originCountry = "USA",
            movieLength = 136,
            hasVideo = true
        )

        val categoryDtos = listOf(
            LocalMovieCategoryDto(categoryId = 2, name = "SCIENCE_FICTION"),
            LocalMovieCategoryDto(categoryId = 0, name = "ALL")
        )

        val expectedGenres = listOf(
            MovieGenre.SCIENCE_FICTION,
            MovieGenre.ALL
        )

        every { movieGenreLocalMapper.toEntityList(categoryDtos) } returns expectedGenres

        val movieWithCategories = MovieWithCategories(
            movie = localMovie,
            categories = categoryDtos
        )

        val expectedDomainMovie = Movie(
            id = 10L,
            name = "The Matrix",
            description = "Sci-fi action",
            posterUrl = "matrix.jpg",
            productionYear = 1999u,
            rating = 8.7f,
            popularity = 80.0,
            originCountry = "USA",
            runTime = 136,
            hasVideo = true,
            categories = expectedGenres
        )

        // Act
        val result = mapper.toEntity(movieWithCategories)

        // Assert (one line only)
        assertThat(result).isEqualTo(expectedDomainMovie)
    }

}
*/
