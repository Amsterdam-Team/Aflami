package com.amsterdam.domain.useCase

import com.amsterdam.domain.exceptions.NoSuggestedCountriesException
import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.domain.validation.CountryValidator
import com.amsterdam.entity.Country

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