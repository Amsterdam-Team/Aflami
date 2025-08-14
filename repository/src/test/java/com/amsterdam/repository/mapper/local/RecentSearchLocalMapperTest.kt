package com.amsterdam.repository.mapper.local

import com.amsterdam.repository.dto.local.SearchLocalDto
import com.amsterdam.repository.mapper.toEntity
import com.amsterdam.repository.mapper.toEntityList
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RecentSearchLocalMapperTest {


    @Test
    @DisplayName("should return searchKeyword from LocalSearchDto")
    fun `toEntity should return searchKeyword`() {
        // Arrange
        val dto = SearchLocalDto(
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
            SearchLocalDto(
                searchKeyword = "Inception",
            ),
            SearchLocalDto(
                searchKeyword = "The Dark Knight",
            )
        )
        // Act
        val result = dtoList.toEntityList()
        // Assert
        assertThat(result).containsExactly("Inception", "The Dark Knight")
    }
}

