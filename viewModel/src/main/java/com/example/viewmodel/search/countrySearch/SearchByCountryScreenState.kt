package com.example.viewmodel.search.countrySearch

data class SearchByCountryScreenState(
    val selectedCountry: String = "",
    val suggestedCountries: List<CountryUiState> = emptyList(),
    val movies: List<MovieUiState> = emptyList()
)
