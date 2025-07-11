package com.example.viewmodel.search.countrySearch

import com.example.entity.Country

data class SearchByCountryUiState(
    val selectedCountry: String = "",
    val suggestedCountries: List<Country> = emptyList(),
    val movies: List<MovieUiState> = emptyList()
)
