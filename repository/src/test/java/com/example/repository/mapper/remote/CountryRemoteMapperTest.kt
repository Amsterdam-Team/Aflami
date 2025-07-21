package com.example.repository.mapper.remote

import com.example.repository.mapper.remote.testFactory.createRemoteCountryDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CountryRemoteMapperTest {

    private lateinit var mapper: CountryRemoteMapper

    @BeforeEach
    fun setUp() {
        mapper = CountryRemoteMapper()
    }

    @Test
    fun `should return Country entity with correct isoCode and nativeName when mapped from RemoteCountryDto`() {
        // Arrange
        val dto = createRemoteCountryDto(
            englishName = "United States",
            isoCode = "US",
            nativeName = "United States of America"
        )

        // Act
        val result = mapper.toEntity(dto)

        // Assert
        assertThat(result.countryIsoCode).isEqualTo("US")
        assertThat(result.countryName).isEqualTo("United States of America")
    }
}
