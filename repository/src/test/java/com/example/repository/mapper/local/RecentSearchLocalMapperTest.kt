/*
package com.example.repository.mapper.local

import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.Instant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RecentSearchLocalMapperTest {

    private lateinit var mapper: RecentSearchLocalMapper

    @BeforeEach
    fun setUp() {
        mapper = RecentSearchLocalMapper()
    }

    @Test
    @DisplayName("should return searchKeyword from LocalSearchDto")
    fun `toEntity should return searchKeyword`() {
        // Arrange
        val dto = LocalSearchDto(
            searchKeyword = "Inception",
            searchType = SearchType.BY_KEYWORD,
            expireDate = Instant.parse("2025-07-20T10:00:00Z")
        )

        // Act
        val result = mapper.toEntity(dto)

        // Assert
        assertThat(result).isEqualTo("Inception")
    }
}
*/
