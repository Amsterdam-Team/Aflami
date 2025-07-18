package com.example.domain.useCase

import com.example.domain.repository.CountryRepository
import com.example.entity.Country

class GetSuggestedCountriesUseCase(
    private val countryRepository: CountryRepository
) {

    suspend operator fun invoke(keyword: String): List<Country> {
        return countryRepository.getCountries()
            .filter {
                it.countryName.contains(keyword, ignoreCase = true)
            }
    }
}