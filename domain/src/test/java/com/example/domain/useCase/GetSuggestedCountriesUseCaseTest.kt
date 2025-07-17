package com.example.domain.useCase


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

class GetSuggestedCountriesUseCaseTest {
    private lateinit var countryRepository: CountryRepository
    private lateinit var getSuggestedCountriesUseCase: GetSuggestedCountriesUseCase

    @BeforeEach
    fun setUp() {
        countryRepository = mockk(relaxed = true)
        getSuggestedCountriesUseCase =
            GetSuggestedCountriesUseCase(countryRepository)
    }

    @Test
    fun `should return a list of suggested countries when a matching keyword is provided`() =
        runTest {
            coEvery { countryRepository.getCountries() } returns fakeCountryList

            val countries = getSuggestedCountriesUseCase("eg")

            assertThat(countries).isNotEmpty()
            coVerify { countryRepository.getCountries() }
        }

    @Test
    fun `should call validateCountry when a country keyword is provided`() {
        runTest {
            coEvery { countryRepository.getCountries() } returns fakeCountryList

            getSuggestedCountriesUseCase("eg")
        }
    }

    @Test
    fun `should return empty list when no countries match the given keyword`() {
        runTest {
            coEvery { countryRepository.getCountries() } returns fakeCountryList

            assertThat(getSuggestedCountriesUseCase("usa")).isEmpty()
        }
    }

    @Test
    fun `should return empty list when the country keyword is empty`() =
        runTest {
            assertThat(getSuggestedCountriesUseCase("")).isEmpty()
        }

    @Test
    fun `should return filtered countries matching keyword case-insensitively when valid input is provided`(): Unit =
        runTest {
            coEvery { countryRepository.getCountries() } returns countriesWithDifferentCases

            val result1 =
                getSuggestedCountriesUseCase(countriesWithDifferentCases[0].countryName[0].toString())
            assertThat(result1).containsExactly(
                Country(
                    countryName = countriesWithDifferentCases[0].countryName,
                    countryIsoCode = countriesWithDifferentCases[0].countryIsoCode
                )
            )

            val result2 =
                getSuggestedCountriesUseCase(countriesWithDifferentCases[1].countryName[1].toString())
            assertThat(result2).containsExactly(
                Country(
                    countryName = countriesWithDifferentCases[1].countryName,
                    countryIsoCode = countriesWithDifferentCases[1].countryIsoCode
                )
            )

        }
}
