package com.amsterdam.viewmodel.search.mapper

import com.amsterdam.entity.Country
import com.amsterdam.viewmodel.search.countrySearch.CountryUiState

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
