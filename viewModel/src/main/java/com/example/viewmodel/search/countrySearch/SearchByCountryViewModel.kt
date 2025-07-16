package com.example.viewmodel.search.countrySearch

import androidx.lifecycle.viewModelScope
import com.example.domain.exceptions.AflamiException
import com.example.domain.useCase.GetMoviesByCountryUseCase
import com.example.domain.useCase.GetSuggestedCountriesUseCase
import com.example.domain.useCase.search.AddRecentSearchUseCase
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

    init {
        observeKeywordFlow()
    }

    private fun observeKeywordFlow() {
        viewModelScope.launch {
            _keyword.debounceSearch(::getCountriesByKeyword)
        }
    }

    private fun getCountriesByKeyword(keyword: String): Job {
        updateState { it.copy(isLoadingCountries = true) }
        return tryToExecute(
            action = { getSuggestedCountriesUseCase(keyword) },
            onSuccess = { countries -> updateSuggestedCountries(countries.toUiState()) },
            onError = (::onError)
        )
    }

    private fun getMoviesByCountry() {
        updateState { it.copy(searchByCountryContentUIState = SearchByCountryContentUIState.LOADING_MOVIES) }
        addRecentSearch()
        tryToExecute(
            action = { getMoviesByCountryUseCase(state.value.selectedCountryIsoCode) },
            onSuccess = { movies -> updateMoviesForCountry(movies.toListOfUiState()) },
            onError = (::onError)
        )
    }

    private fun addRecentSearch() {
        tryToExecute(
            action = { addRecentSearchUseCase.addRecentSearchForCountry(state.value.selectedCountryIsoCode)  },
            onSuccess = { },
            onError = { }
        )
    }

    private fun updateSuggestedCountries(suggestedCountries: List<CountryUiState>) {
        updateState {
            it.copy(
                isLoadingCountries = false,
                suggestedCountries = suggestedCountries,
                isCountriesDropDownVisible = suggestedCountries.isNotEmpty()
            )
        }
    }

    private fun updateMoviesForCountry(movies: List<MovieUiState>) {
        updateState {
            it.copy(
                movies = movies,
                searchByCountryContentUIState =
                    if (movies.isNotEmpty()) SearchByCountryContentUIState.MOVIES_LOADED
                    else SearchByCountryContentUIState.NO_DATA_FOUND
            )
        }
    }

    private fun onError(exception: AflamiException) {
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
        if (keyword.isBlank()) {
            updateState {
                it.copy(
                    keyword = keyword,
                    suggestedCountries = emptyList(),
                    isCountriesDropDownVisible = false
                )
            }
        } else {
            updateState { it.copy(keyword = keyword) }
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
        getMoviesByCountry()
    }

    override fun onNavigateBackClicked() {
        sendNewEffect(SearchByCountryEffect.NavigateBack)
    }

    override fun onRetryRequestClicked() {
        if (state.value.selectedCountryIsoCode.isBlank() && state.value.keyword.isNotBlank()) {
            getCountriesByKeyword(state.value.keyword)
        } else if (state.value.selectedCountryIsoCode.isNotBlank()) {
            getMoviesByCountry()
        } else {
            updateState { it.copy(searchByCountryContentUIState = SearchByCountryContentUIState.COUNTRY_TOUR) }
        }
    }
}