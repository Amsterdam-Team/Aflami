package com.example.remotedatasource

import com.example.remotedatasource.api.CountryApiService
import com.example.remotedatasource.serviceProvider.implementation.CountryServiceProviderImpl
import com.example.repository.dto.remote.RemoteCountryDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach

class CountryServiceProviderImplTest {

    private lateinit var countryApiService: CountryApiService
    private lateinit var countryServiceProviderImpl: CountryServiceProviderImpl

    @BeforeEach
    fun setUp() {
        countryApiService = mockk()
        countryServiceProviderImpl = CountryServiceProviderImpl(countryApiService)
    }

    @Test
    fun `getCountries should call CountryApiService`() = runTest {
        // Given
        val dummyResponse =
            emptyList<RemoteCountryDto>()
        coEvery { countryApiService.getCountries() } returns dummyResponse

        // When
        countryServiceProviderImpl.getCountries()

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
        val actualResponse = countryServiceProviderImpl.getCountries()

        // Then
        assertThat(actualResponse).isEqualTo(expectedResponse)
    }
}