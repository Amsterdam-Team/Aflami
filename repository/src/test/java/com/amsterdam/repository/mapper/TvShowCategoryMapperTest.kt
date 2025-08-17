package com.amsterdam.repository.mapper

import com.amsterdam.repository.dto.local.TvShowCategoryLocalDto
import com.amsterdam.repository.dto.remote.CategoryRemoteDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TvShowCategoryMapperTest {
    @Test
    fun `toLocalTvShowCategoryDto should map CategoryRemoteDto to TvShowCategoryLocalDto`() {
        val storedLanguage = "en"
        val result = remoteDto1.toLocalTvShowCategoryDto(storedLanguage)

        assertThat(result).isEqualTo(expectedLocalCategory1)
    }

    @Test
    fun `toLocalTvShowDtoList should map a list of Dtos to a list of TvShowCategoryLocalDto`() {
        val dtoList = listOf(remoteDto1, remoteDto2)
        val storedLanguage = "ar"
        val result = dtoList.toLocalTvShowDtoList(storedLanguage)

        assertThat(result).isEqualTo(expectedLocalCategoryList)
    }

    @Test
    fun `toLocalTvShowDtoList should return an empty list when given an empty list`() {
        val emptyDtoList = emptyList<CategoryRemoteDto>()
        val result = emptyDtoList.toLocalTvShowDtoList("en")

        assertThat(result).isEmpty()
    }

    private val remoteDto1 = CategoryRemoteDto(id = 10759, name = "Action & Adventure")
    private val remoteDto2 = CategoryRemoteDto(id = 16, name = "Animation")

    private val expectedLocalCategory1 = TvShowCategoryLocalDto(categoryId = 10759L)

    private val expectedLocalCategoryList = listOf(
        TvShowCategoryLocalDto(categoryId = 10759L),
        TvShowCategoryLocalDto(categoryId = 16L)
    )
}