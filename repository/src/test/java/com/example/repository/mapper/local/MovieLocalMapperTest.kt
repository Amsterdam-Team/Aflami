package com.example.repository.mapper.local

import com.example.entity.Movie
import com.example.repository.dto.local.LocalMovieDto
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
  val dto = LocalMovieDto(
   movieId = 101,
   name = "Inception",
   description = "A mind-bending thriller",
   poster = "poster_url.jpg",
   productionYear = 2010,
   rating = 8.8f,
   popularity = 99.5,
   movieLength = 148,
   originCountry = "USA",
   hasVideo = true
  )

  // Act
  val result = mapper.toEntity(dto)

  // Assert
  assertThat(result.id).isEqualTo(101)
  assertThat(result.name).isEqualTo("Inception")
  assertThat(result.description).isEqualTo("A mind-bending thriller")
  assertThat(result.posterUrl).isEqualTo("poster_url.jpg")
  assertThat(result.productionYear).isEqualTo(2010u)
  assertThat(result.rating).isEqualTo(8.8)
  assertThat(result.popularity).isEqualTo(99.5)
  assertThat(result.runTime).isEqualTo(148)
  assertThat(result.originCountry).isEqualTo("USA")
  assertThat(result.hasVideo).isTrue()
  assertThat(result.categories).isEmpty()
 }

 @Test
 @DisplayName("should return LocalMovieDto when converting from Movie entity")
 fun `toDto should return LocalMovieDto when given Movie`() {
  // Arrange
  val entity = Movie(
   id = 202,
   name = "Interstellar",
   description = "Exploration beyond stars",
   posterUrl = "interstellar.jpg",
   productionYear = 2014u,
   rating = 9.0f,
   categories = listOf(), // Not included in dto
   popularity = 95.2,
   runTime = 169,
   originCountry = "USA",
   hasVideo = false
  )

  // Act
  val result = mapper.toDto(entity)

  // Assert
  assertThat(result.movieId).isEqualTo(202)
  assertThat(result.name).isEqualTo("Interstellar")
  assertThat(result.description).isEqualTo("Exploration beyond stars")
  assertThat(result.poster).isEqualTo("interstellar.jpg")
  assertThat(result.productionYear).isEqualTo(2014)
  assertThat(result.rating).isEqualTo(9.0)
  assertThat(result.popularity).isEqualTo(95.2)
  assertThat(result.movieLength).isEqualTo(169)
  assertThat(result.originCountry).isEqualTo("USA")
  assertThat(result.hasVideo).isFalse()
 }
}
