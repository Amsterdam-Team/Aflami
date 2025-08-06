package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.mapper.remoteToLocal.testFactory.createRemoteCategoryDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TvShowCategoryMappersTest {

    @Test
    fun `should map a single RemoteCategoryDto to LocalTvShowCategoryDto correctly`() {
        // Arrange
        val storedLanguage = "en"
        val remoteDto = createRemoteCategoryDto(id = 10759, name = "Action & Adventure")

        // Act
        val localDto = remoteDto.toLocalTvShowCategoryDto(storedLanguage)

        // Assert
        assertThat(localDto.categoryId).isEqualTo(10759L)
        assertThat(localDto.name).isEqualTo("Action & Adventure")
        assertThat(localDto.storedLanguage).isEqualTo("en")
    }

    @Test
    fun `should map a list of RemoteCategoryDto to a list of LocalTvShowCategoryDto correctly`() {
        // Arrange
        val storedLanguage = "es"
        val remoteDtoList = listOf(
            createRemoteCategoryDto(id = 10759, name = "Action & Adventure"),
            createRemoteCategoryDto(id = 35, name = "Comedy")
        )

        // Act
        val localDtoList = remoteDtoList.toLocalTvShowCategoryDtoList(storedLanguage)

        // Assert
        assertThat(localDtoList).hasSize(2)

        val firstCategory = localDtoList[0]
        assertThat(firstCategory.categoryId).isEqualTo(10759L)
        assertThat(firstCategory.name).isEqualTo("Action & Adventure")
        assertThat(firstCategory.storedLanguage).isEqualTo("es")

        val secondCategory = localDtoList[1]
        assertThat(secondCategory.categoryId).isEqualTo(35L)
        assertThat(secondCategory.name).isEqualTo("Comedy")
        assertThat(secondCategory.storedLanguage).isEqualTo("es")
    }
}