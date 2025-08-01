package com.amsterdam.domain.useCase

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.domain.useCase.search.GetSuggestedCountriesUseCase
import com.amsterdam.domain.useCase.utils.countriesWithDifferentCases
import com.amsterdam.domain.useCase.utils.fakeCountryList
import com.amsterdam.entity.Country
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetSuggestedCountriesUseCaseTest {
    private lateinit var countryRepository: CountryRepository
    private lateinit var getSuggestedCountriesUseCase: GetSuggestedCountriesUseCase
    private lateinit var country: Country

    @BeforeEach
    fun setUp() {
        countryRepository = mockk(relaxed = true)
        country = Country("EGYPT", "EG")
        getSuggestedCountriesUseCase = GetSuggestedCountriesUseCase(countryRepository)
    }

    @Test
    fun `should call getCountries exactly once regardless of keyword`() = runTest {
        coEvery { countryRepository.getCountries() } returns emptyList()
        getSuggestedCountriesUseCase("keyword")
        coVerify(exactly = 1) { countryRepository.getCountries() }
    }

    @Test
    fun `should return all countries when keyword is empty`() = runTest {
        coEvery { countryRepository.getCountries() } returns fakeCountryList
        val result = getSuggestedCountriesUseCase("")
        assertThat(result).isEqualTo(fakeCountryList)
    }

    @Test
    fun `should return filtered countries when a matching keyword is provided`() = runTest {
        coEvery { countryRepository.getCountries() } returns fakeCountryList
        val result = getSuggestedCountriesUseCase("EG")
        assertThat(result).hasSize(1)
        assertThat(result.first().countryName).isEqualTo("EGYPT")
    }

    @Test
    fun `should return empty list when no countries match the given keyword`() = runTest {
        coEvery { countryRepository.getCountries() } returns fakeCountryList
        val result = getSuggestedCountriesUseCase("usa")
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return filtered countries matching keyword case-insensitively`() = runTest {
        coEvery { countryRepository.getCountries() } returns countriesWithDifferentCases

        val result = getSuggestedCountriesUseCase("united")
        assertThat(result).containsExactly(countriesWithDifferentCases[0])
    }

    @Test
    fun `should return filtered countries with partial match`() = runTest {
        // Given
        coEvery { countryRepository.getCountries() } returns countriesWithDifferentCases

        // When
        val result = getSuggestedCountriesUseCase("a")

        // Then
        assertThat(result).containsExactly(
            countriesWithDifferentCases[0],
            countriesWithDifferentCases[1],
            countriesWithDifferentCases[2],
            countriesWithDifferentCases[3]
        )
    }

    @Test
    fun `should throw AflamiException when getCountries fails`() = runTest {
        coEvery { countryRepository.getCountries() } throws AflamiException()
        assertThrows<AflamiException> { getSuggestedCountriesUseCase("") }
    }
}