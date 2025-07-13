package com.amsterdam.domain.useCase

import com.amsterdam.domain.exceptions.NoSuggestedCountriesException
import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.domain.validation.CountryValidator
import com.amsterdam.entity.Country
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

    @BeforeEach
    fun setUp() {
        countryRepository = mockk(relaxed = true)
        countryValidator = mockk(relaxed = true)
        getSuggestedCountriesUseCase = GetSuggestedCountriesUseCase(countryRepository, countryValidator)
    }

    @Test
    fun `should return a list of suggested countries`() =
        runBlocking {
            coEvery { countryRepository.getAllCountries() } returns fakeCountryList

            val countries = getSuggestedCountriesUseCase("eg")

            assertThat(countries).isNotEmpty()
            coVerify { countryRepository.getAllCountries() }
        }

    @Test
    fun `should call validateCountry from countryValidator`() {
        runBlocking {
            coEvery { countryValidator.validateCountry(any()) } returns Unit
            coEvery { countryRepository.getAllCountries() } returns fakeCountryList

            getSuggestedCountriesUseCase("eg")
        }
    }

    @Test
    fun `should throw NoSuggestedCountriesException when there is no suggested countries with the given keyword`() {
        runBlocking {
            coEvery { countryRepository.getAllCountries() } returns fakeCountryList

            assertThrows<NoSuggestedCountriesException> {
                getSuggestedCountriesUseCase("usa")
            }
        }
    }
}
