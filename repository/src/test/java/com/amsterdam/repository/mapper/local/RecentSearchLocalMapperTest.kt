package com.amsterdam.repository.mapper.local

import com.amsterdam.repository.dto.local.LocalSearchDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RecentSearchLocalMapperTest {


    @Test
    @DisplayName("should return searchKeyword from LocalSearchDto")
    fun `toEntity should return searchKeyword`() {
        // Arrange
        val dto = LocalSearchDto(
            searchKeyword = "Inception",
        )

        // Act
        val result = dto.toEntity()

        // Assert
        assertThat(result).isEqualTo("Inception")
    }

    @Test
    @DisplayName("should return list of searchKeywords from list of LocalSearchDto")
    fun `toEntityList should return list of searchKeywords`() {
        // Arrange
        val dtoList = listOf(
            LocalSearchDto(
                searchKeyword = "Inception",
            ),
            LocalSearchDto(
                searchKeyword = "The Dark Knight",
            )
        )
        // Act
        val result = dtoList.toEntityList()
        // Assert
        assertThat(result).containsExactly("Inception", "The Dark Knight")
    }
}

