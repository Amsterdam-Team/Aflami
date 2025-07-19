package com.example.localdatasource

import com.example.localdatasource.roomDataBase.daos.CountryDao
import com.example.localdatasource.roomDataBase.datasource.CountryLocalDataSourceImpl
import com.example.repository.dto.local.LocalCountryDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class CountryLocalDataSourceImplTest {
    private lateinit var dao: CountryDao
    private lateinit var countryLocalDataSourceImpl: CountryLocalDataSourceImpl

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        countryLocalDataSourceImpl = CountryLocalDataSourceImpl(dao)
    }

    @Test
    fun `getCountries should return data from dao`() = runTest {
        //Given
        val expected = listOf(LocalCountryDto("1", "Egypt"), LocalCountryDto("2", "Palestine"))
        //When
        coEvery { dao.getAllCountries() } returns expected
        val result = countryLocalDataSourceImpl.getCountries()
        //Then
        assertEquals(expected, result)

    }

    @Test
    fun `getCountries should return emptyList from dao`() = runTest {
        //Given
        val expected = emptyList<LocalCountryDto>()
        //When
        coEvery { dao.getAllCountries() } returns expected
        val result = countryLocalDataSourceImpl.getCountries()
        //Then
        assertEquals(expected, result)
    }

    @Test
    fun `addCountries should call dao with correct data`() = runTest {
        //Given
        val countries = listOf(LocalCountryDto("1", "Egypt"), LocalCountryDto("2", "Palestine"))
        //When
        countryLocalDataSourceImpl.addCountries(countries)
        //Then
        coEvery { dao.upsertAllCountries(countries) }

    }
}