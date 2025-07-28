package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.CountryApiService
import com.amsterdam.repository.dto.remote.RemoteCountryDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CountryRemoteDataSourceImplTest {

    private lateinit var countryApiService: CountryApiService
    private lateinit var countryRemoteDataSourceImpl: CountryRemoteDataSourceImpl

    @BeforeEach
    fun setUp() {
        countryApiService = mockk()
        countryRemoteDataSourceImpl = CountryRemoteDataSourceImpl(countryApiService)
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
            RemoteCountryDto("Country1", "Capital1", "Region1"),
            RemoteCountryDto("Country2", "Capital2", "Region2")
        )
        coEvery { countryApiService.getCountries() } returns expectedResponse

        // When
        val actualResponse = countryRemoteDataSourceImpl.getCountries()

        // Then
        assertThat(actualResponse).isEqualTo(expectedResponse)
    }

}