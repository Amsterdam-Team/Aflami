package com.amsterdam.repository.mapper

import com.amsterdam.repository.dto.local.SearchLocalDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class RecentSearchMapperTest {
    @Nested
    inner class ToEntityTest {
        @Test
        fun `toEntity should map SearchLocalDto to its searchKeyword string`() {
            val result = searchDto1.toEntity()

            assertThat(result).isEqualTo("The Matrix")
        }
    }

    @Nested
    inner class ToEntityListTest {
        @Test
        fun `toEntityList should map a list of SearchLocalDto to a list of strings`() {
            val dtoList = listOf(searchDto1, searchDto2)

            val result = dtoList.toEntityList()

            assertThat(result).containsExactly("The Matrix", "Inception").inOrder()
        }

        @Test
        fun `toEntityList should return an empty list when given an empty list`() {
            val emptyDtoList = emptyList<SearchLocalDto>()

            val result = emptyDtoList.toEntityList()

            assertThat(result).isEmpty()
        }
    }

    private val searchDto1 = SearchLocalDto(searchKeyword = "The Matrix")
    private val searchDto2 = SearchLocalDto(searchKeyword = "Inception")
}