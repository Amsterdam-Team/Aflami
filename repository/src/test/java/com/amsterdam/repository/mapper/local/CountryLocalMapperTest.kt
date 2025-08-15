package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Country
import com.amsterdam.repository.dto.local.CountryLocalDto
import com.amsterdam.repository.mapper.toEntity
import com.amsterdam.repository.mapper.toLocalDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class CountryLocalMapperTest {
    @Test
    fun `toEntity should return Country entity when given LocalCountryDto`() {
        val result = countryLocalDto.toEntity()

        assertThat(result.countryName).isEqualTo(EGYPT_NAME)
        assertThat(result.countryIsoCode).isEqualTo(EGYPT_ISO_CODE)
    }

    @Test
    fun `toDto should return LocalCountryDto when given Country entity`() {
        val result = countryEntity.toLocalDto(STORED_LANGUAGE)

        assertThat(result.name).isEqualTo(FRANCE_NAME)
        assertThat(result.isoCode).isEqualTo(FRANCE_ISO_CODE)
    }

    companion object {
        private const val EGYPT_NAME = "Egypt"
        private const val EGYPT_ISO_CODE = "EG"
        private const val STORED_LANGUAGE = "en"
        private const val FRANCE_NAME = "France"
        private const val FRANCE_ISO_CODE = "FR"

        private val countryLocalDto = CountryLocalDto(
            name = EGYPT_NAME,
            isoCode = EGYPT_ISO_CODE,
            storedLanguage = STORED_LANGUAGE,
        )

        private val countryEntity = Country(
            countryName = FRANCE_NAME,
            countryIsoCode = FRANCE_ISO_CODE
        )
    }
}