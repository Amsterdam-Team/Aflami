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

        // Act
        val localDto = remoteDto.toLocalMovieCategoryDto()

        // Assert
        assertThat(localDto.categoryId).isEqualTo(12L)
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

        // Act
        val localDtoList = remoteDtoList.toLocalDtoList()

        // Assert
        assertThat(localDtoList).hasSize(2)

        val firstCategory = localDtoList[0]
        assertThat(firstCategory.categoryId).isEqualTo(10759L)

        val secondCategory = localDtoList[1]
        assertThat(secondCategory.categoryId).isEqualTo(35L)
    }
}