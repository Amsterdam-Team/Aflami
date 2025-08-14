package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.CountryDao
import com.amsterdam.repository.dto.local.CountryLocalDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class CountryLocalDataSourceImplTest {
    private val dao by lazy { mockk<CountryDao>(relaxed = true) }
    private val countryLocalDataSourceImpl by lazy { CountryLocalDataSourceImpl(dao) }

    @Test
    fun `getCountries should return data from dao when there is data returned`() = runTest {
        coEvery { dao.getAllCountries(englishStoredLanguage) } returns countries

        val result = countryLocalDataSourceImpl.getCountries(englishStoredLanguage)

        assertThat(result).isEqualTo(countries)
    }

    @Test
    fun `getCountries should return emptyList from dao when there is no data returned`() = runTest {
        coEvery { dao.getAllCountries(englishStoredLanguage) } returns emptyList()

        val result = countryLocalDataSourceImpl.getCountries(englishStoredLanguage)

        assertThat(result).isEmpty()
    }

    @Test
    fun `addCountries should call dao with correct data`() = runTest {
        countryLocalDataSourceImpl.upsertCountries(countries)

        coVerify(exactly = 1) { dao.upsertAllCountries(countries) }
    }
}

private val countries =
    listOf(
        CountryLocalDto("1", "Egypt", "en"),
        CountryLocalDto("2", "Palestine", "en")
    )

private const val englishStoredLanguage = "en"