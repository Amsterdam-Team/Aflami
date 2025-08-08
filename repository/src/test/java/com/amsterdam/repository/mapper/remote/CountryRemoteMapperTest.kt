package com.amsterdam.repository.mapper.remote

import com.amsterdam.repository.mapper.remote.testFactory.createRemoteCountryDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class CountryRemoteMapperTest {

    @Test
    fun `should return Country entity with correct isoCode and nativeName when mapped from RemoteCountryDto`() {
        // Arrange
        val dto = createRemoteCountryDto(
            englishName = "United States",
            isoCode = "US",
            nativeName = "United States of America"
        )

        // Act
        val result = dto.toEntity()

        // Assert
        assertThat(result.countryIsoCode).isEqualTo("US")
        assertThat(result.countryName).isEqualTo("United States of America")
    }
}
