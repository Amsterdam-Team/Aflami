/*
package com.amsterdam.repository.mapper.local

import com.amsterdam.repository.mapper.local.testFactory.createCategory
import com.amsterdam.repository.mapper.local.testFactory.createLocalMovieCategoryDto
import com.amsterdam.entity.Category
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MovieCategoryLocalMapperTest {

    private lateinit var mapper: MovieCategoryLocalMapper

    @BeforeEach
    fun setUp() {
        mapper = MovieCategoryLocalMapper()
    }

    @Test
    fun `toEntity should return Category when given LocalMovieCategoryDto`() {
        // Arrange
        val dto = createLocalMovieCategoryDto(
            categoryId = 1,
            name = "Action"
        )
        val expected = Category(
            id = 1,
            name = "Action",
            imageUrl = ""
        )

        // Act
        val result = mapper.toEntity(dto)

        // Assert
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `toDto should return LocalMovieCategoryDto when given Category`() {
        // Arrange
        val entity = createCategory(
            id = 5,
            name = "Drama",
            imageUrl = "some_url"
        )
        val expected = LocalMovieCategoryDto(
            categoryId = 5,
            name = "Drama"
        )

        // Act
        val result = mapper.toDto(entity)

        // Assert
        assertThat(result).isEqualTo(expected)
    }
}
*/
