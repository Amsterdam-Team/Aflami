package com.example.viewmodel.search.countrySearch

data class SearchByCountryUiState(
    val selectedCountry: String = "",
    val suggestedCountries: List<String> = emptyList(),
    val movies: List<MovieUiState> = emptyList()
)
