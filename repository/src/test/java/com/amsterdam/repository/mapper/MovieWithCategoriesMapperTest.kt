package com.amsterdam.repository.mapper

import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.repository.dto.local.MovieCategoryLocalDto
import com.amsterdam.repository.dto.local.MovieLocalDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class MovieWithCategoriesMapperTest {
    @Test
    fun `toEntity should map MovieWithCategories to Movie entity with distinct categories`() {
        val result = movieWithCategories.toEntity()

        assertThat(result).isEqualTo(expectedMovie)
    }

    @Test
    fun `toEntity should handle duplicate categories by using distinctBy`() {
        val result = movieWithDuplicateCategories.toEntity()

        assertThat(result.categories).containsExactly(MovieGenre.ACTION, MovieGenre.DRAMA).inOrder()
        assertThat(result.id).isEqualTo(expectedMovie.id)
        assertThat(result.name).isEqualTo(expectedMovie.name)
    }

    @Test
    fun `toEntity should handle an empty list of categories`() {
        val result = movieWithNoCategories.toEntity()

        assertThat(result.id).isEqualTo(101L)
        assertThat(result.name).isEqualTo("Test Movie")
        assertThat(result.categories).isEmpty()
    }

    private val baseMovieLocalDto = MovieLocalDto(
        movieId = 101L,
        storedLanguage = "en",
        name = "Test Movie",
        description = "An overview.",
        poster = "/poster.jpg",
        releaseDate = LocalDate.parse("2023-10-26"),
        popularity = 1500.0,
        rating = 8.5f,
        originCountry = "US",
        movieLength = 120,
        isAdult = false
    )

    private val movieWithCategories = MovieWithCategories(
        movie = baseMovieLocalDto,
        categories = listOf(
            MovieCategoryLocalDto(categoryId = 28L),
            MovieCategoryLocalDto(categoryId = 18L)
        )
    )

    private val movieWithDuplicateCategories = MovieWithCategories(
        movie = baseMovieLocalDto,
        categories = listOf(
            MovieCategoryLocalDto(categoryId = 28L),
            MovieCategoryLocalDto(categoryId = 18L),
            MovieCategoryLocalDto(categoryId = 28L)
        )
    )

    private val movieWithNoCategories = MovieWithCategories(
        movie = baseMovieLocalDto,
        categories = emptyList()
    )

    private val expectedMovie = Movie(
        id = 101L,
        name = "Test Movie",
        description = "An overview.",
        posterUrl = "/poster.jpg",
        releaseDate = LocalDate.parse("2023-10-26"),
        rating = 8.5f,
        categories = listOf(MovieGenre.ACTION, MovieGenre.DRAMA),
        popularity = 1500.0,
        runTimeInMinutes = 120,
        originCountry = "US"
    )
}