package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.ProductionCompany
import com.amsterdam.repository.mapper.remote.testFactory.createProductionCompanyDto
import com.amsterdam.repository.utils.ImageBaseUrlsConstant.BASE_IMAGE_URL_W500
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
        val fullPath = BASE_IMAGE_URL_W500 + logoPath

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
