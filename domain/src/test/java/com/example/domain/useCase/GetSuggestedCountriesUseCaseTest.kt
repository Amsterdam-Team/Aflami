package com.example.domain.useCase


import com.example.domain.exceptions.AflamiException
import com.example.domain.repository.CountryRepository
import com.example.domain.useCase.utils.countriesWithDifferentCases
import com.example.domain.useCase.utils.fakeCountryList
import com.example.entity.Country
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
    fun `getSuggestedCountriesUseCase should call getCountries exactly one time`() = runTest {
        coEvery { countryRepository.getCountries() } returns emptyList()
        getSuggestedCountriesUseCase("keyword")
        coVerify(exactly = 1) { countryRepository.getCountries() }
    }

    @Test
    fun `getSuggestedCountriesUseCase should return a list of suggested countries when a matching keyword is provided`() =
        runTest {
            coEvery { countryRepository.getCountries() } returns fakeCountryList
            val countries = getSuggestedCountriesUseCase("EG")
            assertThat(countries).isNotEmpty()
        }

    @Test
    fun `getSuggestedCountriesUseCase should return empty list when no countries match the given keyword`() {
        runTest {
            coEvery { countryRepository.getCountries() } returns fakeCountryList
            assertThat(getSuggestedCountriesUseCase("usa")).isEmpty()
        }
    }

    @Test
    fun `getSuggestedCountriesUseCase should return filtered countries matching keyword case-insensitively when valid input is provided`(): Unit =
        runTest {
            coEvery { countryRepository.getCountries() } returns countriesWithDifferentCases

            val result = getSuggestedCountriesUseCase("U")
            assertThat(result).contains(
                countriesWithDifferentCases[0]
            )
        }
    @Test
    fun `getSuggestedCountriesUseCase should return Aflami exception when an error happened`() = runTest {
        coEvery { countryRepository.getCountries() } throws AflamiException()
        assertThrows<AflamiException> { getSuggestedCountriesUseCase("") }
    }
}
