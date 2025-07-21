package com.example.remotedatasource

import com.example.remotedatasource.api.CountryApiService
import com.example.remotedatasource.datasource.CountryRemoteDataSourceImpl
import com.example.repository.dto.remote.RemoteCountryDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class CountryRemoteDataSourceImplTest {
    private lateinit var countryApiService: CountryApiService
    private lateinit var countryRemoteDataSourceImpl: CountryRemoteDataSourceImpl


    private val jsonSerializer =
        Json { ignoreUnknownKeys = true }

    @Before
    fun setUp() {
        countryApiService = mockk()
        countryRemoteDataSourceImpl = CountryRemoteDataSourceImpl(countryApiService)
    }

    @Test
    fun `getCountries should return a list of countries when executed`() = runTest {
        //Given
        val jsonString = """
    [
      {"iso_3166_1": "EG", "english_name": "Egypt", "native_name": "مصر"},
      {"iso_3166_1": "US", "english_name": "United States", "native_name": "United States"}
    ]
""".trimIndent()

        val expectedCountriesDtoList =
            jsonSerializer.decodeFromString<List<RemoteCountryDto>>(jsonString)

        coEvery {
            countryApiService.getCountries()
        } returns expectedCountriesDtoList

        //When
        val countries = countryRemoteDataSourceImpl.getCountries()

        //Then
        assertEquals("EG", countries[0].isoCode)
        assertEquals("United States", countries[1].englishName)
        assertEquals(2, countries.size)
    }
}