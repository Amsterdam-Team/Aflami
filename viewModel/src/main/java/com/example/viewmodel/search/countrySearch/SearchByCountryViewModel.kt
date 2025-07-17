package com.example.viewmodel.search.countrySearch

import androidx.lifecycle.viewModelScope
import com.example.domain.exceptions.AflamiException
import com.example.domain.useCase.GetMoviesByCountryUseCase
import com.example.domain.useCase.GetSuggestedCountriesUseCase
import com.example.domain.useCase.search.AddRecentSearchUseCase
import com.example.entity.Country
import com.example.entity.Movie
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.search.mapper.toListOfUiState
import com.example.viewmodel.search.mapper.toSearchByCountryState
import com.example.viewmodel.search.mapper.toUiState
import com.example.viewmodel.utils.debounceSearch
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.isNotEmpty

class SearchByCountryViewModel(
    private val getSuggestedCountriesUseCase: GetSuggestedCountriesUseCase,
    private val getMoviesByCountryUseCase: GetMoviesByCountryUseCase,
    private val addRecentSearchUseCase: AddRecentSearchUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<SearchByCountryScreenState, SearchByCountryEffect>(
    SearchByCountryScreenState(),
    dispatcherProvider
), SearchByCountryInteractionListener {
    private val _keyword = MutableStateFlow("")

    init { observeKeywordFlow() }

    private fun observeKeywordFlow() {
        viewModelScope.launch { _keyword.debounceSearch(::fetchCountriesByKeyword) }
    }

    private fun fetchCountriesByKeyword(keyword: String): Job {
        updateState { it.copy(isLoadingCountries = true) }
        return tryToExecute(
            action = { getSuggestedCountriesUseCase(keyword) },
            onSuccess = ::onFetchCountriesSuccess,
            onError = ::onFetchError
        )
    }

    private fun onFetchCountriesSuccess(countries: List<Country>) {
        updateState {
            it.copy(
                isLoadingCountries = false,
                suggestedCountries = countries.toUiState(),
                isCountriesDropDownVisible = countries.isNotEmpty()
            )
        }
    }

    private fun onFetchError(exception: AflamiException) {
        updateState {
            it.copy(
                isLoadingCountries = false,
                suggestedCountries = emptyList(),
                movies = emptyList(),
                searchByCountryContentUIState = exception.toSearchByCountryState()
            )
        }
    }

    override fun onKeywordValueChanged(keyword: String) {
        _keyword.update { keyword }
        updateState {
            it.copy(
                keyword = keyword,
                suggestedCountries = if (keyword.isBlank()) emptyList() else it.suggestedCountries,
                isCountriesDropDownVisible = !keyword.isBlank()
            )
        }
    }

    override fun onCountrySelected(country: CountryUiState) {
        updateState {
            it.copy(
                keyword = country.countryName,
                selectedCountryIsoCode = country.countryIsoCode,
                isCountriesDropDownVisible = false
            )
        }
        fetchMoviesByCountry()
    }

    override fun onRetryRequestClicked() {
        val hasKeyword = state.value.keyword.isNotBlank()
        val hasSelectedCountry = state.value.selectedCountryIsoCode.isNotBlank()

        when {
            !hasSelectedCountry && hasKeyword -> fetchCountriesByKeyword(state.value.keyword)
            hasSelectedCountry -> fetchMoviesByCountry()
            else -> updateState {
                it.copy(searchByCountryContentUIState = SearchByCountryContentUIState.COUNTRY_TOUR)
            }
        }
    }

    private fun fetchMoviesByCountry() {
        updateState { it.copy(searchByCountryContentUIState = SearchByCountryContentUIState.LOADING_MOVIES) }

        viewModelScope.launch { addRecentSearchUseCase(state.value.selectedCountryIsoCode) }

        tryToExecute(
            action = { getMoviesByCountryUseCase(state.value.selectedCountryIsoCode) },
            onSuccess = ::onFetchMoviesSuccess,
            onError = ::onFetchError
        )
    }

    private fun onFetchMoviesSuccess(movies: List<Movie>) {
        updateState {
            it.copy(
                movies = movies.toListOfUiState(),
                searchByCountryContentUIState = when {
                    movies.isEmpty() -> SearchByCountryContentUIState.NO_DATA_FOUND
                    else -> SearchByCountryContentUIState.MOVIES_LOADED
                }
            )
        }
    }

    override fun onNavigateBackClicked() {
        sendNewEffect(SearchByCountryEffect.NavigateBack)
    }
}