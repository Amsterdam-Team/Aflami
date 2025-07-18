package com.example.viewmodel.search.countrySearch

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NetworkException
import com.example.viewmodel.shared.uiStates.MovieItemUiState

data class CountrySearchUiState(
    val keyword: String = "",
    val selectedCountryIsoCode: String = "",
    val suggestedCountries: List<CountryItemUiState> = emptyList(),
    val movies: List<MovieItemUiState> = emptyList(),
    val isLoading: Boolean = false,
    val isCountriesDropDownVisible: Boolean = false,
    val errorUiState: CountrySearchErrorState? = null,
)

data class CountryItemUiState(
    val countryName: String,
    val countryIsoCode: String
)

sealed interface CountrySearchErrorState {
    object NoNetworkConnection : CountrySearchErrorState
    object UnknownError : CountrySearchErrorState

    companion object{
        fun toCountrySearchErrorState(exception: AflamiException): CountrySearchErrorState {
            return when (exception) {
                is NetworkException, -> NoNetworkConnection
                else -> UnknownError
            }
        }
    }
}
