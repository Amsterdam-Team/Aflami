/*
package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Category
import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TvShowCategoryLocalMapperTest {

    private lateinit var mapper: TvShowCategoryLocalMapper

    @BeforeEach
    fun setUp() {
        mapper = TvShowCategoryLocalMapper()
    }

    @Test
    @DisplayName("should map LocalTvShowCategoryDto to Category correctly")
    fun `toEntity should map correctly`() {
        // Arrange
        val dto = LocalTvShowCategoryDto(
            categoryId = 1,
            name = "Drama"
        )

        // Act
        val result = mapper.toEntity(dto)

        // Assert
        assertThat(result.id).isEqualTo(1)
        assertThat(result.name).isEqualTo("Drama")
        assertThat(result.imageUrl).isEqualTo("")
    }

    @Test
    @DisplayName("should map Category to LocalTvShowCategoryDto correctly")
    fun `toDto should map correctly`() {
        // Arrange
        val category = Category(
            id = 2,
            name = "Comedy",
            imageUrl = "https://example.com/image.jpg"
        )

        // Act
        val result = mapper.toDto(category)

        // Assert
        assertThat(result.categoryId).isEqualTo(2)
        assertThat(result.name).isEqualTo("Comedy")
    }
}
*/
