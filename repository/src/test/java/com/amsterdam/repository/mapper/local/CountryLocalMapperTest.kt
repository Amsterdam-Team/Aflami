
package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Country
import com.amsterdam.repository.dto.local.LocalCountryDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class CountryLocalMapperTest {


    @Test
    fun `toEntity should return Country entity when given LocalCountryDto`() {
        // When
        val dto = LocalCountryDto(
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
        val result = entity.LocalCountryDto("en")

        // Then
        assertThat(result.name).isEqualTo("France")
        assertThat(result.isoCode).isEqualTo("FR")
    }
    @Test
    fun `test list of LocalCountryDto is correctly mapped to list of Country`() {
        // Given
        val dtoList = listOf(
            LocalCountryDto(name = "Egypt", isoCode = "EG", storedLanguage = "ar"),
            LocalCountryDto(name = "Germany", isoCode = "DE", storedLanguage = "de")
        )

        // When
        val countryList = dtoList.toEntityList()

        // Then
        assertThat(countryList).hasSize(2)

        assertThat(countryList[0].countryName).isEqualTo("Egypt")
        assertThat(countryList[0].countryIsoCode).isEqualTo("EG")

        assertThat(countryList[1].countryName).isEqualTo("Germany")
        assertThat(countryList[1].countryIsoCode).isEqualTo("DE")
    }

}
