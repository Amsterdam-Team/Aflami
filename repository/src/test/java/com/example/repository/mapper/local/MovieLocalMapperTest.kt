package com.example.repository.mapper.local

import com.example.entity.Movie
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MovieLocalMapperTest {

    private val mapper = MovieLocalMapper(CategoryLocalMapper())



    @Test
    fun `should return Movie with all fields and categories when mapping from LocalMovieDto`() {
        val dto = LocalMovieDto(
            movieId = 1L,
            name = "Inception",
            description = "Dream within a dream",
            poster = "inception.jpg",
            productionYear = 2010,
            rating = 8.8f,
            popularity = 0.0
        )
        val categories = listOf(LocalMovieCategoryDto(1L, "Sci-Fi"))

        val result = mapper.mapToMovie(MovieWithCategories(dto, categories))

        assertThat(result.id).isEqualTo(1L)
        assertThat(result.name).isEqualTo("Inception")
        assertThat(result.description).isEqualTo("Dream within a dream")
        assertThat(result.posterUrl).isEqualTo("inception.jpg")
        assertThat(result.productionYear).isEqualTo(2010)
        assertThat(result.rating).isEqualTo(8.8f)
    }

    @Test
    fun `should return Movie with empty categories when mapping from LocalMovieDto without categories`() {
        val dto = LocalMovieDto(
            movieId = 2L,
            name = "Titanic",
            description = "Romantic tragedy",
            poster = "titanic.jpg",
            productionYear = 1997,
            rating = 7.9f,
            popularity = 0.0
        )

        val result = mapper.mapToMovie(MovieWithCategories(dto, emptyList()))

        assertThat(result.categories).isEmpty()
    }

    @Test
    fun `should return list of Movies with correct categories when mapping from LocalMovieDto list`() {
        val dtos = listOf(
            LocalMovieDto(1L, "Movie A", "Desc A", "a.jpg", 2001, popularity = 0.0, rating = 7.1f),
            LocalMovieDto(2L, "Movie B", "Desc B", "b.jpg", 2002, popularity = 0.0, rating = 7.1f)
        )
        val categories = listOf(
            listOf(LocalMovieCategoryDto(1L, "Action")),
            listOf(LocalMovieCategoryDto(2L, "Drama" ))
        )
        val moviesWithCategories = dtos.mapIndexed { index, localMovieDto ->  MovieWithCategories(localMovieDto, categories[index])}
        val result = mapper.mapToMovies(moviesWithCategories)

        assertThat(result).hasSize(2)
    }

    @Test
    fun `should return list of Movies with empty categories when categories map is empty`() {
        val dtos = listOf(
            LocalMovieDto(1L, "Movie A", "Desc A", "a.jpg", 2001, popularity = 0.0, rating = 7.1f)
        )

        val moviesWithCategories = dtos.map { localMovieDto ->  MovieWithCategories(localMovieDto, emptyList())}

        val result = mapper.mapToMovies(moviesWithCategories)

        assertThat(result).hasSize(1)
        assertThat(result[0].categories).isEmpty()
    }

    @Test
    fun `should return list of LocalMovieDto when mapping from list of Movies`() {
        val domains = listOf(
            Movie(1L, "Movie A", "Desc A", "a.jpg", 2001, emptyList(),7.1f, 0.0),
            Movie(2L, "Movie B", "Desc B", "b.jpg", 2002, emptyList(), 7.2f, 0.0)
        )

        val mapper = MovieLocalMapper(CategoryLocalMapper())

        val result = mapper.mapToLocalMovies(domains)

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Movie A")
        assertThat(result[1].name).isEqualTo("Movie B")
    }


    @Test
    fun `should return empty Movie list when mapping empty LocalMovieDto list`() {
        val result = mapper.mapToMovies(emptyList())

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty LocalMovieDto list when mapping empty Movie list`() {
        val result = mapper.mapToLocalMovies(emptyList())

        assertThat(result).isEmpty()
    }
}
