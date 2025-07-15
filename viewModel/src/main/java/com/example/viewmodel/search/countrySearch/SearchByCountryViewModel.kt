package com.example.viewmodel.search.countrySearch

import androidx.lifecycle.viewModelScope
import com.example.domain.exceptions.AflamiException
import com.example.domain.useCase.GetMoviesByCountryUseCase
import com.example.domain.useCase.GetSuggestedCountriesUseCase
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.search.mapper.toListOfUiState
import com.example.viewmodel.search.mapper.toSearchByCountryState
import com.example.viewmodel.search.mapper.toUiState
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchByCountryViewModel(
    private val getSuggestedCountriesUseCase: GetSuggestedCountriesUseCase,
    private val getMoviesByCountryUseCase: GetMoviesByCountryUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<SearchByCountryScreenState, SearchByCountryEffect>(
    SearchByCountryScreenState(),
    dispatcherProvider
), SearchByCountryInteractionListener {
    private val _keyword = MutableStateFlow("")

    init {
        observeKeywordFlow()
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun observeKeywordFlow() {
        viewModelScope.launch {
            _keyword
                .debounce(DEBOUNCE_DURATION)
                .map(String::trim)
                .filter(String::isNotBlank)
                .collectLatest(::getCountriesByKeyword)
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
        tryToExecute(
            action = { getMoviesByCountryUseCase(state.value.selectedCountryIsoCode) },
            onSuccess = { movies -> updateMoviesForCountry(movies.toListOfUiState()) },
            onError = (::onError)
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

    override fun onRetryQuestClicked() {
        if (state.value.selectedCountryIsoCode.isBlank() && state.value.keyword.isNotBlank()) {
            updateState { it.copy(isLoadingCountries = true) }
            getCountriesByKeyword(state.value.keyword)
        } else if (state.value.selectedCountryIsoCode.isNotBlank()) {
            updateState { it.copy(searchByCountryContentUIState = SearchByCountryContentUIState.LOADING_MOVIES) }
            getMoviesByCountry()
        } else {
            updateState { it.copy(searchByCountryContentUIState = SearchByCountryContentUIState.COUNTRY_TOUR) }
        }
    }

    companion object {
        private const val DEBOUNCE_DURATION = 300L
    }
}