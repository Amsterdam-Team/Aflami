package com.example.domain.usecase

import com.example.domain.exceptions.CountryTooShortException
import com.example.domain.exceptions.NoSuggestedCountriesException
import com.example.domain.repository.CountryRepository
import com.example.entity.Country

class GetSuggestedCountriesUseCase(private val countryRepository: CountryRepository) {

    suspend operator fun invoke(keyword: String): List<Country> {
        if (keyword.length < 2) throw CountryTooShortException()
        val countries = countryRepository.getSuggestedCountries()
        return findCountryByName(countries, keyword).takeIf { it.isNotEmpty() }
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