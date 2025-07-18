package com.example.viewmodel.search.countrySearch

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.domain.exceptions.AflamiException
import com.example.domain.useCase.GetMoviesByCountryUseCase
import com.example.domain.useCase.GetSuggestedCountriesUseCase
import com.example.domain.useCase.RecentSearchesUseCase
import com.example.entity.Country
import com.example.entity.Movie
import com.example.viewmodel.search.mapper.toCountry
import com.example.viewmodel.search.mapper.toMoveUiStates
import com.example.viewmodel.search.mapper.toUiState
import com.example.viewmodel.shared.BaseViewModel
import com.example.viewmodel.utils.debounceSearch
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CountrySearchViewModel(
    private val getSuggestedCountriesUseCase: GetSuggestedCountriesUseCase,
    private val getMoviesByCountryUseCase: GetMoviesByCountryUseCase,
    private val recentSearchesUseCase: RecentSearchesUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<CountrySearchUiState, CountrySearchEffect>(
    CountrySearchUiState(),
    dispatcherProvider
), CountrySearchInteractionListener {
    private val _keyword = MutableStateFlow("")

    init { observeKeywordFlow() }

    private fun observeKeywordFlow() {
        viewModelScope.launch { _keyword.debounceSearch(::fetchCountriesByKeyword) }
    }

    private fun fetchCountriesByKeyword(keyword: String): Job {
        updateState { it.copy(isLoading = true) }
        return tryToExecute(
            action = { getSuggestedCountriesUseCase(keyword) },
            onSuccess = ::onFetchCountriesSuccess,
            onError = ::onFetchError
        )
    }
    private fun onFetchCountriesSuccess(countries: List<Country>) {
        Log.e("bk", countries.toString())
        updateState { it.copy(suggestedCountries = countries.toUiState(), isCountriesDropDownVisible = true, errorUiState = null) }
    }

    private fun onFetchError(exception: AflamiException) {
        Log.e("bk", "exception: $exception")

        updateState {
            it.copy(
                isLoading = false,
                suggestedCountries = emptyList(),
                movies = emptyList(),
                errorUiState = CountrySearchErrorState.toCountrySearchErrorState(exception)
            )
        }
    }

    override fun onChangeSearchKeyword(keyword: String) {
        _keyword.update { keyword }

        updateState {
            it.copy(
                keyword = keyword,
                isLoading = keyword.isNotBlank(),
                isCountriesDropDownVisible = it.suggestedCountries.isNotEmpty(),
                suggestedCountries = if (keyword.isBlank()) emptyList() else it.suggestedCountries,
                errorUiState = null,
                movies = emptyList()
            )
        }
    }

    override fun onSelectCountry(country: CountryItemUiState) {
        updateState {
            it.copy(
                keyword = country.countryName,
                selectedCountryIsoCode = country.countryIsoCode,
                isCountriesDropDownVisible = false
            )
        }
        fetchMoviesByCountry(getSelectedCountry())
    }

    override fun onClickRetry() {
        val hasKeyword = state.value.keyword.isNotBlank()
        val hasSelectedCountry = state.value.selectedCountryIsoCode.isNotBlank()

        when {
            !hasSelectedCountry && hasKeyword -> fetchCountriesByKeyword(state.value.keyword)
            hasSelectedCountry -> fetchMoviesByCountry(getSelectedCountry())

        }
    }

    override fun onClickMovieCard(movieId: Long) {
        updateState { it.copy(selectedMovieId = movieId) }
        sendNewEffect(CountrySearchEffect.NavigateToMovieDetails)
    }

    private fun fetchMoviesByCountry(selectedCountry: Country) {
        updateState { it.copy(isLoading = true) }

        viewModelScope.launch { recentSearchesUseCase.addRecentSearchForCountry(selectedCountry) }

        tryToExecute(
            action = { getMoviesByCountryUseCase(selectedCountry) },
            onSuccess = ::onFetchMoviesSuccess,
            onError = ::onFetchError
        )
    }

    private fun getSelectedCountry(): Country {
        return state.value.suggestedCountries.find {
            state.value.selectedCountryIsoCode == it.countryIsoCode
        }.toCountry()
    }

    private fun onFetchMoviesSuccess(movies: List<Movie>) {
        updateState { it.copy(movies = movies.toMoveUiStates(), isLoading = false,) }
    }

    override fun onClickNavigateBack() {
        sendNewEffect(CountrySearchEffect.NavigateBack)
    }
}