package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.CountryApiService
import com.amsterdam.repository.dto.remote.CountryRemoteDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CountryRemoteDataSourceImplTest {

    private val countryApiService: CountryApiService = mockk()
    private val countryRemoteDataSourceImpl: CountryRemoteDataSourceImpl =
        CountryRemoteDataSourceImpl(countryApiService)

    @Test
    fun `getCountries should return the correct list of countries on success`() = runTest {
        coEvery { countryApiService.getCountries() } returns expectedCountryList

        val actualResponse = countryRemoteDataSourceImpl.getCountries()

        assertThat(actualResponse).isEqualTo(expectedCountryList)
    }

    @Test
    fun `getCountries should call the CountryApiService exactly once on success`() = runTest {
        coEvery { countryApiService.getCountries() } returns expectedCountryList

        countryRemoteDataSourceImpl.getCountries()

        coVerify(exactly = 1) { countryApiService.getCountries() }
    }

    @Test
    fun `getCountries should return an empty list when the API service returns one`() = runTest {
        coEvery { countryApiService.getCountries() } returns emptyList()

        val actualResponse = countryRemoteDataSourceImpl.getCountries()

        assertThat(actualResponse).isEmpty()
    }

    @Test
    fun `getCountries should call the API service exactly once when it returns an empty list`() =
        runTest {
            coEvery { countryApiService.getCountries() } returns emptyList()

            countryRemoteDataSourceImpl.getCountries()

            coVerify(exactly = 1) { countryApiService.getCountries() }
        }

    @Test
    fun `getCountries should throw NetworkException when the API service call fails`() = runTest {
        coEvery { countryApiService.getCountries() } throws networkException

        assertThrows<NetworkException> {
            countryRemoteDataSourceImpl.getCountries()
        }
    }

    @Test
    fun `getCountries should rethrow a NetworkException when the API service throws an exception`() =
        runTest {
            coEvery { countryApiService.getCountries() } throws networkException

            assertThrows<NetworkException> {
                countryRemoteDataSourceImpl.getCountries()
            }
        }

    private val expectedCountryList = listOf(
        CountryRemoteDto(
            englishName = "United States of America",
            isoCode = "US",
            nativeName = "United States"
        ),
        CountryRemoteDto(englishName = "Canada", isoCode = "CA", nativeName = "Canada")
    )

    private val networkException = NetworkException()
}