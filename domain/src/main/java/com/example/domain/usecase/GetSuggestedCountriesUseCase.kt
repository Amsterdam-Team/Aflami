package com.example.domain.usecase

import com.example.domain.repository.CountryRepository
import com.example.entity.Country

class GetSuggestedCountriesUseCase(private val countryRepository: CountryRepository) {

    suspend operator fun invoke(): List<Country> {
        val countries = countryRepository.getSuggestedCountries()
        return countries.map { Country(it.countryName, it.countryIsoCode) }
    }

}
