package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Category
import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MovieWithCategoryMapperTest {

    private val mapper = MovieWithCategoryMapper()

    @Test
    fun `should return Movie with all fields and mapped categories when mapping from MovieWithCategories`() {
        val localMovie = LocalMovieDto(
            movieId = 1L,
            name = "Inception",
            description = "Dream inside a dream",
            poster = "inception.jpg",
            productionYear = 2010,
            rating = 8.8f,
            popularity = 0.0
        )

        val localCategories = listOf(
            LocalMovieCategoryDto(1L, "Sci-Fi"),
            LocalMovieCategoryDto(2L, "Thriller")
        )

        val movieWithCategories = MovieWithCategories(
            movie = localMovie,
            categories = localCategories
        )

        val result = mapper.mapFromLocal(movieWithCategories)

        assertThat(result.id).isEqualTo(1L)
        assertThat(result.name).isEqualTo("Inception")
        assertThat(result.description).isEqualTo("Dream inside a dream")
        assertThat(result.poster).isEqualTo("inception.jpg")
        assertThat(result.productionYear).isEqualTo(2010)
        assertThat(result.rating).isEqualTo(8.8f)
        assertThat(result.categories).hasSize(2)
        assertThat(result.categories).containsExactly(
            Category(1L, "Sci-Fi", ""),
            Category(2L, "Thriller", "")
        )
    }

    @Test
    fun `should return Movie with empty categories when MovieWithCategories has no categories`() {
        val localMovie = LocalMovieDto(
            movieId = 2L,
            name = "Titanic",
            description = "Romantic tragedy",
            poster = "titanic.jpg",
            productionYear = 1997,
            rating = 7.9f,
            popularity = 0.0
        )

        val movieWithCategories = MovieWithCategories(
            movie = localMovie,
            categories = emptyList()
        )

        val result = mapper.mapFromLocal(movieWithCategories)

        assertThat(result.categories).isEmpty()
    }

    @Test
    fun `should return LocalMovieDto with same fields when mapping from Movie`() {
        val movie = Movie(
            id = 3L,
            name = "Interstellar",
            description = "Space travel",
            poster = "interstellar.jpg",
            productionYear = 2014,
            rating = 8.6f,
            popularity = 0.0,
            categories = listOf(Category(1L, "Adventure", "adventure.png"))
        )

        val result = mapper.mapToLocal(movie)

        assertThat(result.movieId).isEqualTo(3L)
        assertThat(result.name).isEqualTo("Interstellar")
        assertThat(result.description).isEqualTo("Space travel")
        assertThat(result.poster).isEqualTo("interstellar.jpg")
        assertThat(result.productionYear).isEqualTo(2014)
        assertThat(result.rating).isEqualTo(8.6f)
    }
}
