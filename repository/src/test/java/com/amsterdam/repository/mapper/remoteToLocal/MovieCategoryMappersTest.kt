package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.mapper.remoteToLocal.testFactory.createRemoteCategoryDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test


class MovieCategoryMappersTest {

    @Test
    fun `should map a single RemoteCategoryDto to LocalMovieCategoryDto correctly`() {
        // Arrange
        val remoteDto = createRemoteCategoryDto(
            id = 12,
            name = "Adventure"
        )
        val storedLanguage = "en"

        // Act
        val localDto = remoteDto.toLocalMovieCategoryDto(storedLanguage)

        // Assert
        assertThat(localDto.categoryId).isEqualTo(12L)
        assertThat(localDto.name).isEqualTo("Adventure")
        assertThat(localDto.storedLanguage).isEqualTo("en")
    }

    @Test
    fun `should map a list of RemoteCategoryDto to a list of LocalMovieCategoryDto correctly`() {
        // Arrange
        val remoteDtoList = listOf(
            createRemoteCategoryDto(
                id = 10759,
                name = "Action & Adventure"
            ),
            createRemoteCategoryDto(
                id = 35,
                name = "Comedy"
            )
        )
        val storedLanguage = "es"

        // Act
        val localDtoList = remoteDtoList.toLocalDtoList(storedLanguage)

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