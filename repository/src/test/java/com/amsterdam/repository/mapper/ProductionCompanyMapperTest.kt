package com.amsterdam.repository.mapper

import com.amsterdam.entity.ProductionCompany
import com.amsterdam.repository.dto.remote.ProductionCompanyRemoteDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ProductionCompanyMapperTest {
    @Test
    fun `toEntity should map Dto to ProductionCompany entity`() {
        val result = remoteDtoWithLogo.toEntity()

        assertThat(result).isEqualTo(expectedEntityWithLogo)
    }

    @Test
    fun `toEntity should map imageUrl to empty string when fullLogoPath is null`() {
        val result = remoteDtoWithoutLogo.toEntity()

        assertThat(result).isEqualTo(expectedEntityWithoutLogo)
    }

    @Test
    fun `toEntityList should map a list of Dtos to a list of ProductionCompany entities`() {
        val dtoList = listOf(remoteDtoWithLogo, remoteDtoWithoutLogo)
        val result = dtoList.toEntityList()

        assertThat(result).isEqualTo(expectedEntityList)
    }

    @Test
    fun `toEntityList should return an empty list when given an empty list`() {
        val emptyDtoList = emptyList<ProductionCompanyRemoteDto>()
        val result = emptyDtoList.toEntityList()

        assertThat(result).isEmpty()
    }

    private val remoteDtoWithLogo = ProductionCompanyRemoteDto(
        id = 1L,
        logoPath = "/disney.png",
        name = "Walt Disney Pictures",
        originCountry = "US"
    )

    private val remoteDtoWithoutLogo = ProductionCompanyRemoteDto(
        id = 2L,
        logoPath = null,
        name = "Marvel Studios",
        originCountry = "US"
    )

    private val expectedEntityWithLogo = ProductionCompany(
        id = 1L,
        imageUrl = "https://image.tmdb.org/t/p/w500/disney.png",
        name = "Walt Disney Pictures",
        country = "US"
    )

    private val expectedEntityWithoutLogo = ProductionCompany(
        id = 2L,
        imageUrl = "",
        name = "Marvel Studios",
        country = "US"
    )

    private val expectedEntityList = listOf(
        expectedEntityWithLogo,
        expectedEntityWithoutLogo
    )
}