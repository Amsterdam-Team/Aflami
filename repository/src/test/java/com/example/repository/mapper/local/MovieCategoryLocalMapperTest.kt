package com.example.repository.mapper.local

import com.example.entity.Category
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MovieCategoryLocalMapperTest {

 private lateinit var mapper: MovieCategoryLocalMapper

 @BeforeEach
 fun setUp() {
  mapper = MovieCategoryLocalMapper()
 }

 @Test
 @DisplayName("should return Category entity when converting from LocalMovieCategoryDto")
 fun `toEntity should return Category when given LocalMovieCategoryDto`() {
  // Arrange
  val dto = LocalMovieCategoryDto(
   categoryId = 1,
   name = "Action"
  )

  // Act
  val result = mapper.toEntity(dto)

  // Assert
  assertThat(result.id).isEqualTo(1)
  assertThat(result.name).isEqualTo("Action")
  assertThat(result.imageUrl).isEqualTo("") // default value
 }

 @Test
 @DisplayName("should return LocalMovieCategoryDto when converting from Category entity")
 fun `toDto should return LocalMovieCategoryDto when given Category`() {
  // Arrange
  val entity = Category(
   id = 5,
   name = "Drama",
   imageUrl = "some_url"
  )

  // Act
  val result = mapper.toDto(entity)

  // Assert
  assertThat(result.categoryId).isEqualTo(5)
  assertThat(result.name).isEqualTo("Drama")
 }
}
