
package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Country
import com.amsterdam.repository.dto.local.CountryLocalDto
import com.amsterdam.repository.mapper.toEntity
import com.amsterdam.repository.mapper.toEntityList
import com.amsterdam.repository.mapper.toLocalDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class CountryLocalMapperTest {


    @Test
    fun `toEntity should return Country entity when given LocalCountryDto`() {
        // When
        val dto = CountryLocalDto(
            name = "Egypt",
            isoCode = "EG",
            storedLanguage = "en",
        )

        // When
        val result = dto.toEntity()

        // Then
        assertThat(result.countryName).isEqualTo("Egypt")
        assertThat(result.countryIsoCode).isEqualTo("EG")
    }

    @Test
    fun `toDto should return LocalCountryDto when given Country entity`() {
        // Given
        val entity = Country(
            countryName = "France",
            countryIsoCode = "FR"
        )

        // When
        val result = entity.toLocalDto("en")

        // Then
        assertThat(result.name).isEqualTo("France")
        assertThat(result.isoCode).isEqualTo("FR")
    }
}
