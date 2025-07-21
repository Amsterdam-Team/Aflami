package com.example.repository.mapper.local

import com.example.entity.Country
import com.example.repository.dto.local.LocalCountryDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CountryLocalMapperTest {

    private lateinit var mapper: CountryLocalMapper

    @BeforeEach
    fun setUp() {
        mapper = CountryLocalMapper()
    }

    @Test
    fun `toEntity should return Country entity when given LocalCountryDto`() {
        // Arrange
        val dto = LocalCountryDto(
            name = "Egypt",
            isoCode = "EG"
        )

        // Act
        val result = mapper.toEntity(dto)

        // Assert
        assertThat(result.countryName).isEqualTo("Egypt")
        assertThat(result.countryIsoCode).isEqualTo("EG")
    }

    @Test
    fun `toDto should return LocalCountryDto when given Country entity`() {
        // Arrange
        val entity = Country(
            countryName = "France",
            countryIsoCode = "FR"
        )

        // Act
        val result = mapper.toDto(entity)

        // Assert
        assertThat(result.name).isEqualTo("France")
        assertThat(result.isoCode).isEqualTo("FR")
    }
}
