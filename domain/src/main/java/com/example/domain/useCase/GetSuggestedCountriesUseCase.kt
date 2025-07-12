package com.example.domain.useCase

import com.example.domain.exceptions.NoSuggestedCountriesException
import com.example.domain.repository.CountryRepository
import com.example.domain.validation.CountryValidator
import com.example.entity.Country

class GetSuggestedCountriesUseCase(
    private val countryRepository: CountryRepository,
    private val countryValidator: CountryValidator
) {

    suspend operator fun invoke(keyword: String): List<Country> {
        countryValidator.validateCountry(keyword)
        val countries = countryRepository.getAllCountries()
        return findCountryByName(countries, keyword.trim()).takeIf { it.isNotEmpty() }
            ?: throw NoSuggestedCountriesException()
    }

    private fun findCountryByName(
        countries: List<Country>,
        keyword: String
    ): List<Country> {
        return countries.filter {
            it.countryName.contains(keyword, ignoreCase = true)
        }
    }

}