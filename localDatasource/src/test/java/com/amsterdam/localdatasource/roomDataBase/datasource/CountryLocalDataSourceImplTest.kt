package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.CountryDao
import com.amsterdam.repository.dto.local.CountryLocalDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CountryLocalDataSourceImplTest {
    private lateinit var dao: CountryDao
    private lateinit var countryLocalDataSourceImpl: CountryLocalDataDataSourceImpl

    @BeforeEach
    fun setup() {
        dao = mockk(relaxed = true)
        countryLocalDataSourceImpl = CountryLocalDataDataSourceImpl(dao)
    }

    @Test
    fun `getCountries should return data from dao`() = runTest {
        //Given
        val expected = listOf(CountryLocalDto("1", "Egypt","en"), CountryLocalDto("2", "Palestine","en"))
        //When
        coEvery { dao.getAllCountries("en") } returns expected
        val result = countryLocalDataSourceImpl.getCountries("en")
        //Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getCountries should return emptyList from dao`() = runTest {
        //Given
        val expected = emptyList<CountryLocalDto>()
        //When
        coEvery { dao.getAllCountries("en") } returns expected
        val result = countryLocalDataSourceImpl.getCountries("en")
        //Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `addCountries should call dao with correct data`() = runTest {
        //Given
        val countries = listOf(CountryLocalDto("1", "Egypt","en"), CountryLocalDto("2", "Palestine","en"))
        //When
        countryLocalDataSourceImpl.upsertCountries(countries)
        // Then
        coVerify (exactly = 1) { dao.upsertAllCountries(countries) }

    }
}