package com.amsterdam.viewmodel.search.countrySearch

import androidx.paging.PagingData
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CountrySearchUiState(
    val keyword: String = "",
    val selectedCountryIsoCode: String = "",
    val showSuggestedCountries: Boolean = false,
    val suggestedCountries: List<CountryItemUiState> = emptyList(),
    val movies: Flow<PagingData<MovieItemUiState>> = emptyFlow(),
    val isLoading: Boolean = false,
    val errorUiState: CountrySearchErrorState? = null,
    val selectedMovieId: Long = 0
)

data class CountryItemUiState(
    val countryName: String,
    val countryIsoCode: String
)

sealed interface CountrySearchErrorState {
    data object NoNetworkConnection : CountrySearchErrorState
    data object UnknownError : CountrySearchErrorState
    companion object {
        fun toCountrySearchErrorState(exception: Throwable): CountrySearchErrorState {
            return when (exception) {
                is NetworkException -> NoNetworkConnection
                else -> UnknownError
            }
        }
    }
}
