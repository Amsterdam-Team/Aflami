package com.amsterdam.domain.useCase.search

import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.entity.Country

class GetSuggestedCountriesUseCase (
    private val countryRepository: CountryRepository
) {

    suspend operator fun invoke(keyword: String): List<Country> {
        return countryRepository.getCountries()
            .filter {
                it.countryName.contains(keyword, ignoreCase = true)
            }
    }
}