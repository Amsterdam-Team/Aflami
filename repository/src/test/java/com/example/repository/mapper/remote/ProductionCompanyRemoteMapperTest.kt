package com.example.repository.mapper.remote

import com.example.entity.ProductionCompany
import com.example.repository.BuildConfig
import com.example.repository.mapper.remote.testFactory.createProductionCompanyDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProductionCompanyRemoteMapperTest {

    private lateinit var mapper: ProductionCompanyRemoteMapper

    @BeforeEach
    fun setUp() {
        mapper = ProductionCompanyRemoteMapper()
    }

    @Test
    fun `toEntity should correctly map ProductionCompanyDto to ProductionCompany`() {
        val logoPath = "/logo.png"
        val fullPath = BuildConfig.BASE_IMAGE_URL + logoPath

        val dto = createProductionCompanyDto(
            id = 101,
            logoPath = logoPath,
            name = "Studio Ghibli",
            originCountry = "JP"
        )

        val result = mapper.toEntity(dto)

        val expected = ProductionCompany(
            id = 101,
            imageUrl = fullPath,
            name = "Studio Ghibli",
            country = "JP"
        )

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `toEntity should return empty imageUrl when logoPath is null`() {
        val dto = createProductionCompanyDto(
            logoPath = null
        )

        val result = mapper.toEntity(dto)

        assertThat(result.imageUrl).isEmpty()
    }
}
