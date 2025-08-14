package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.CountryApiService
import com.amsterdam.repository.dto.remote.CountryRemoteDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CountryRemoteDataSourceImplTest {

    private lateinit var countryApiService: CountryApiService
    private lateinit var countryRemoteDataSourceImpl: CountryRemoteDataDataSourceImpl

    @BeforeEach
    fun setUp() {
        countryApiService = mockk()
        countryRemoteDataSourceImpl = CountryRemoteDataDataSourceImpl(countryApiService)
    }

    @Test
    fun `getCountries should call CountryApiService`() = runTest {
        // Given
        coEvery { countryApiService.getCountries() } returns emptyList()

        // When
        countryRemoteDataSourceImpl.getCountries()

        // Then
        coVerify(exactly = 1) { countryApiService.getCountries() }
    }

    @Test
    fun `getCountries should return the correct data from CountryApiService`() = runTest {
        // Given
        val expectedResponse = listOf(
            CountryRemoteDto(
                englishName = "United States of America",
                isoCode = "US",
                nativeName = "United States"
            ),
            CountryRemoteDto(englishName = "Canada", isoCode = "CA", nativeName = "Canada")
        )
        coEvery { countryApiService.getCountries() } returns expectedResponse

        // When
        val actualResponse = countryRemoteDataSourceImpl.getCountries()

        // Then
        assertThat(actualResponse).isEqualTo(expectedResponse)
    }

    @Test
    fun `getCountries should throw NetworkException when API service throws an exception`() =
        runTest {
            // Given
            coEvery { countryApiService.getCountries() } throws NetworkException()

            // When & Then
            assertThrows<NetworkException> {
                countryRemoteDataSourceImpl.getCountries()
            }
        }

    @Test
    fun `getCountries should return an empty list when API service returns an empty list`() =
        runTest {
            // Given
            val expectedResponse = emptyList<CountryRemoteDto>()
            coEvery { countryApiService.getCountries() } returns expectedResponse

            // When
            val actualResponse = countryRemoteDataSourceImpl.getCountries()

            // Then
            assertThat(actualResponse).isEmpty()
            coVerify(exactly = 1) { countryApiService.getCountries() }
        }
}