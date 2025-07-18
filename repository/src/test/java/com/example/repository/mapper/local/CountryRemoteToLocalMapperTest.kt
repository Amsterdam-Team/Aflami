package com.example.repository.mapper.local

import com.example.entity.Country
import com.example.repository.dto.local.LocalCountryDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class CountryRemoteToLocalMapperTest {

    private val mapper = CountryLocalMapper()

    @Test
    fun `should return Country with same name and isoCode when mapping from LocalCountryDto`() {
        val dto = LocalCountryDto(name = "Egypt", isoCode = "EG")

        val result = mapper.toCountry(dto)

        assertThat(result.countryName).isEqualTo("Egypt")
        assertThat(result.countryIsoCode).isEqualTo("EG")
    }

    @Test
    fun `should return LocalCountryDto with same name and isoCode when mapping from Country`() {
        val domain = Country(countryName = "France", countryIsoCode = "FR")

        val result = mapper.toLocalCountry(domain)

        assertThat(result.name).isEqualTo("France")
        assertThat(result.isoCode).isEqualTo("FR")
    }

    @Test
    fun `should return Country with empty values when mapping from empty LocalCountryDto`() {
        val dto = LocalCountryDto(name = "", isoCode = "")

        val result = mapper.toCountry(dto)

        assertThat(result.countryName).isEmpty()
        assertThat(result.countryIsoCode).isEmpty()
    }

    @Test
    fun `should return LocalCountryDto with empty values when mapping from Country with empty fields`() {
        val domain = Country(countryName = "", countryIsoCode = "")

        val result = mapper.toLocalCountry(domain)

        assertThat(result.name).isEmpty()
        assertThat(result.isoCode).isEmpty()
    }
}
