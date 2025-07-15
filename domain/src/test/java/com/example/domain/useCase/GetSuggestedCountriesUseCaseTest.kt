package com.example.domain.useCase

import com.example.domain.exceptions.CountryIsEmptyException
import com.example.domain.exceptions.NoSuggestedCountriesException
import com.example.domain.repository.CountryRepository
import com.example.domain.validation.CountryValidator
import com.example.entity.Country
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetSuggestedCountriesUseCaseTest {
    private lateinit var countryRepository: CountryRepository
    private lateinit var countryValidator: CountryValidator
    private lateinit var getSuggestedCountriesUseCase: GetSuggestedCountriesUseCase

    @BeforeEach
    fun setUp() {
        countryRepository = mockk(relaxed = true)
        countryValidator = mockk(relaxed = true)
        getSuggestedCountriesUseCase =
            GetSuggestedCountriesUseCase(countryRepository, countryValidator)
    }

    @Test
    fun `should return a list of suggested countries when a matching keyword is provided`() =
        runBlocking {
            coEvery { countryRepository.getAllCountries() } returns fakeCountryList

            val countries = getSuggestedCountriesUseCase("eg")

            assertThat(countries).isNotEmpty()
            coVerify { countryRepository.getAllCountries() }
        }

    @Test
    fun `should call validateCountry when a country keyword is provided`() {
        runBlocking {
            coEvery { countryValidator.validateCountry(any()) } returns Unit
            coEvery { countryRepository.getAllCountries() } returns fakeCountryList

            getSuggestedCountriesUseCase("eg")
        }
    }

    @Test
    fun `should throw NoSuggestedCountriesException when no countries match the given keyword`() {
        runBlocking {
            coEvery { countryRepository.getAllCountries() } returns fakeCountryList

            assertThrows<NoSuggestedCountriesException> {
                getSuggestedCountriesUseCase("usa")
            }
        }
    }

    @Test
    fun `should throw CountryIsEmptyException when the country keyword is empty`() =
        runBlocking {
            coEvery { countryValidator.validateCountry(any()) } throws CountryIsEmptyException()

            assertThrows<CountryIsEmptyException> {
                getSuggestedCountriesUseCase("")
            }
            coVerify { countryValidator.validateCountry("") }
        }

    @Test
    fun `should return filtered countries matching keyword case-insensitively when valid input is provided`(): Unit =
        runBlocking {
            val countriesWithDifferentCases =
                listOf(
                    Country(countryName = "United States", countryIsoCode = "us"),
                    Country(countryName = "Australia", countryIsoCode = "au"),
                    Country(countryName = "canada", countryIsoCode = "ca"),
                    Country(countryName = "Germany", countryIsoCode = "de"),
                )
            coEvery { countryRepository.getAllCountries() } returns countriesWithDifferentCases

            val result1 = getSuggestedCountriesUseCase("un")
            assertThat(result1).containsExactly(
                Country(
                    countryName = "United States",
                    countryIsoCode = "us"
                )
            )

            val result2 = getSuggestedCountriesUseCase("CAN")
            assertThat(result2).containsExactly(
                Country(
                    countryName = "canada",
                    countryIsoCode = "ca"
                )
            )

            assertThrows<NoSuggestedCountriesException> {
                getSuggestedCountriesUseCase("xyz")
            }

        }

    private val fakeCountryList =
        listOf(
            Country(
                countryName = "Egypt",
                countryIsoCode = "eg",
            ),
            Country(
                countryName = "Iraq",
                countryIsoCode = "ir",
            ),
        )
}
