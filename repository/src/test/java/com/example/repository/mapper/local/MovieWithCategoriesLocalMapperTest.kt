package com.example.repository.mapper.local

import com.example.entity.Movie
import com.example.entity.category.MovieGenre
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.relation.MovieWithCategories
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
  val localMovieDto = LocalMovieDto(
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
   movie = localMovieDto,
   categories = categoryDtos
  )

  // Act
  val result: Movie = mapper.toEntity(movieWithCategories)

  // Assert
  assertThat(result.id).isEqualTo(10L)
  assertThat(result.name).isEqualTo("The Matrix")
  assertThat(result.description).isEqualTo("Sci-fi action")
  assertThat(result.posterUrl).isEqualTo("matrix.jpg")
  assertThat(result.productionYear).isEqualTo(1999u)
  assertThat(result.rating).isEqualTo(8.7f)
  assertThat(result.popularity).isEqualTo(80.0)
  assertThat(result.originCountry).isEqualTo("USA")
  assertThat(result.runTime).isEqualTo(136)
  assertThat(result.hasVideo).isTrue()
  assertThat(result.categories).containsExactlyElementsIn(expectedGenres)
 }
}
