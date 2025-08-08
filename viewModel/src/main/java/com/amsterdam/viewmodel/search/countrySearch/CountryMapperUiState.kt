package com.amsterdam.viewmodel.search.countrySearch

import com.amsterdam.entity.Country

fun Country.toUiState(): CountryItemUiState {
    return CountryItemUiState(
        countryName = this.countryName,
        countryIsoCode = this.countryIsoCode
    )
}

fun List<Country>.toUiState(): List<CountryItemUiState> {
    return this.map { country ->
        country.toUiState()
    }
}

fun CountryItemUiState?.toCountry(): Country {
    return Country(
        countryName = this?.countryName ?: "",
        countryIsoCode = this?.countryIsoCode ?: ""
    )
}
