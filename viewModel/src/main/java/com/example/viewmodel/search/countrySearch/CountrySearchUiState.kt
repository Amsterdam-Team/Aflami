package com.example.viewmodel.search.countrySearch

import com.example.viewmodel.shared.MovieItemUiState

data class CountrySearchUiState(
    val keyword: String = "",
    val selectedCountryIsoCode: String = "",
    val suggestedCountries: List<CountryItemUiState> = emptyList(),
    val movies: List<MovieItemUiState> = emptyList(),
    val isLoadingCountries: Boolean = false,
    val isCountriesDropDownVisible: Boolean = false,
    val searchByCountryContentUIState: SearchByCountryContentUIState = SearchByCountryContentUIState.COUNTRY_TOUR,
)

data class CountryItemUiState(
    val countryName: String,
    val countryIsoCode: String
)

enum class SearchByCountryContentUIState {
    COUNTRY_TOUR,
    LOADING_MOVIES,
    MOVIES_LOADED,
    NO_INTERNET_CONNECTION,
    NO_DATA_FOUND,
}
