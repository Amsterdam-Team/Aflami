package com.amsterdam.localdatasource.daos

import com.amsterdam.localdatasource.roomDataBase.daos.CountryDao
import com.amsterdam.repository.dto.local.CountryLocalDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CountryDaoTest : BaseDaoTest() {
    private lateinit var countryDao: CountryDao

    @BeforeEach
    fun setup() {
        countryDao = aflamiDatabase.countryDao()
    }

    @Test
    fun upsertAllCountries_shouldAddLocalCountryDtoList_whenCalled() = runTest {
        countryDao.upsertAllCountries(countriesWithSameLanguage)
        val stored = countryDao.getAllCountries(countriesWithSameLanguage.first().storedLanguage)

        assertThat(stored).isEqualTo(countriesWithSameLanguage)
    }

    @Test
    fun upsertAllCountries_shouldUpdateItem_whenPrimaryKeyExists() = runTest {
        countryDao.upsertAllCountries(initialCountries)
        countryDao.upsertAllCountries(updatedCountries)
        val stored = countryDao.getAllCountries(initialCountries.first().storedLanguage)

        assertThat(stored.first().name).isEqualTo(updatedCountries.first().name)
    }

    @Test
    fun getAllCountries_shouldReturnOnlyMatchingLanguageEntries() = runTest {
        countryDao.upsertAllCountries(countriesWithDifferentLanguage)

        val resultEn = countryDao.getAllCountries(countriesWithDifferentLanguage[0].storedLanguage)

        assertThat(resultEn).containsExactly(countriesWithDifferentLanguage[0])
    }

    @Test
    fun getAllCountries_shouldReturnEmpty_whenNoDataMatchesLanguage() = runTest {
        countryDao.upsertAllCountries(initialCountries)

        val result = countryDao.getAllCountries("fr")

        assertThat(result).isEmpty()
    }

    @Test
    fun getAllCountries_shouldReturnEmpty_whenNoDataInserted() = runTest {
        val result = countryDao.getAllCountries("en")

        assertThat(result).isEmpty()
    }
}

private val countriesWithSameLanguage = listOf(
    CountryLocalDto(isoCode = "US", storedLanguage = "en", name = "United States"),
    CountryLocalDto(isoCode = "FR", storedLanguage = "en", name = "France")
)

private val initialCountries = listOf(
    CountryLocalDto(isoCode = "US", storedLanguage = "en", name = "United States")
)

private val updatedCountries = listOf(
    CountryLocalDto(isoCode = "US", storedLanguage = "en", name = "USA")
)

private val countriesWithDifferentLanguage = listOf(
    CountryLocalDto(isoCode = "US", storedLanguage = "en", name = "USA"),
    CountryLocalDto(isoCode = "EG", storedLanguage = "ar", name = "مصر")
)