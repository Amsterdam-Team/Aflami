package com.example.viewmodel.search.mapper

import com.example.entity.Country
import com.example.viewmodel.search.countrySearch.CountryUiState

fun Country.toUiState(): CountryUiState {
    return CountryUiState(
        countryName = this.countryName,
        countryIsoCode = this.countryIsoCode
    )
}

fun List<Country>.toUiState(): List<CountryUiState> {
    return this.map { country ->
        country.toUiState()
    }
}

fun CountryUiState.toCountry(): Country {
    return Country(
        countryName = this.countryName,
        countryIsoCode = this.countryIsoCode
    )
}
