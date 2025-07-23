package com.example.remotedatasource

import com.example.domain.exceptions.NetworkException
import com.example.domain.exceptions.NoInternetException
import com.example.domain.exceptions.ServerErrorException
import com.example.remotedatasource.datasource.CountryRemoteDataSourceImpl
import com.example.remotedatasource.serviceProvider.CountryServiceProvider
import com.example.repository.dto.remote.RemoteCountryDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows


class CountryRemoteDataSourceImplTest {
    private lateinit var countryServiceProvider: CountryServiceProvider
    private lateinit var countryRemoteDataSourceImpl: CountryRemoteDataSourceImpl


    private val jsonSerializer =
        Json { ignoreUnknownKeys = true }

    @BeforeEach
    fun setUp() {
        countryServiceProvider = mockk()
        countryRemoteDataSourceImpl = CountryRemoteDataSourceImpl(countryServiceProvider)
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
            countryServiceProvider.getCountries()
        } returns expectedCountriesDtoList

        //When
        val countries = countryRemoteDataSourceImpl.getCountries()

        //Then
        assertThat(countries[0].isoCode).isEqualTo("EG")
        assertThat(countries[1].englishName).isEqualTo("United States")
        assertThat(countries.size).isEqualTo(2)
    }

    @Test
    fun `getCountries should rethrow ServerErrorException from service provider when exception occurs`() =
        runTest {
            // Given
            coEvery { countryServiceProvider.getCountries() } throws ServerErrorException()

            // When & Then
            assertThrows<ServerErrorException> {
                countryRemoteDataSourceImpl.getCountries()
            }
        }

    @Test
    fun `getCountries should rethrow NoInternetException from service provider when exception occurs`() =
        runTest {
            // Given
            coEvery { countryServiceProvider.getCountries() } throws NoInternetException()

            // When & Then
            assertThrows<NoInternetException> {
                countryRemoteDataSourceImpl.getCountries()
            }
        }

    @Test
    fun `getCountries should rethrow NetworkException from service provider when exception occurs`() =
        runTest {
            // Given
            coEvery { countryServiceProvider.getCountries() } throws NetworkException()

            // When & Then
            assertThrows<NetworkException> {
                countryRemoteDataSourceImpl.getCountries()
            }
        }
}