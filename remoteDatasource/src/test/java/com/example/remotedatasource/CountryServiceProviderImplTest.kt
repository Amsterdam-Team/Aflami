package com.example.remotedatasource

import com.example.remotedatasource.api.CountryApiService
import com.example.remotedatasource.serviceProvider.implementation.CountryServiceProviderImpl
import com.example.repository.dto.remote.RemoteCountryDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CountryServiceProviderImplTest {

    private lateinit var countryApiService: CountryApiService
    private lateinit var countryServiceProviderImpl: CountryServiceProviderImpl

    @Before
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
}