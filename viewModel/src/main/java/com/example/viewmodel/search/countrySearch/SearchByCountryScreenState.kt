package com.example.viewmodel.search.countrySearch

data class SearchByCountryScreenState(
    val keyword: String = "",
    val selectedCountry: CountryUiState = CountryUiState(),
    val suggestedCountries: List<CountryUiState> = emptyList(),
    val movies: List<MovieUiState> = emptyList(),
    val isLoadingCountries: Boolean = false,
    val isCountriesDropDownVisible: Boolean = false,
    val searchByCountryContentUIState: SearchByCountryContentUIState = SearchByCountryContentUIState.COUNTRY_TOUR,
    val selectedMovieId : Long = 0
)

enum class SearchByCountryContentUIState {
    COUNTRY_TOUR,
    LOADING_MOVIES,
    MOVIES_LOADED,
    NO_INTERNET_CONNECTION,
    NO_DATA_FOUND,
}
