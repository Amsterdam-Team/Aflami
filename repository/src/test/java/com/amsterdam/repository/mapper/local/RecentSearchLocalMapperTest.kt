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
        val dto = singleSearchDto

        val result = dto.toEntity()

        assertThat(result).isEqualTo(SEARCH_KEYWORD_1)
    }

    @Test
    @DisplayName("should return list of searchKeywords from list of LocalSearchDto")
    fun `toEntityList should return list of searchKeywords`() {
        val dtoList = searchDtoList

        val result = dtoList.toEntityList()

        assertThat(result).containsExactlyElementsIn(expectedKeywords).inOrder()
    }

    companion object {
        private const val SEARCH_KEYWORD_1 = "Inception"
        private const val SEARCH_KEYWORD_2 = "The Dark Knight"

        private val singleSearchDto = SearchLocalDto(
            searchKeyword = SEARCH_KEYWORD_1,
        )

        private val searchDtoList = listOf(
            SearchLocalDto(searchKeyword = SEARCH_KEYWORD_1),
            SearchLocalDto(searchKeyword = SEARCH_KEYWORD_2)
        )

        private val expectedKeywords = listOf(SEARCH_KEYWORD_1, SEARCH_KEYWORD_2)
    }
}