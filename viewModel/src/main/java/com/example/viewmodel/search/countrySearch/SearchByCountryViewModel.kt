package com.example.viewmodel.search.countrySearch

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.domain.exceptions.AflamiException
import com.example.domain.useCase.GetMoviesByCountryUseCase
import com.example.domain.useCase.GetSuggestedCountriesUseCase
import com.example.paging.PagingSource
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.search.mapper.toSearchByCountryState
import com.example.viewmodel.search.mapper.toUiState
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
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
            action = {
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            getMoviesByCountryUseCase(state.value.selectedCountryIsoCode, page)
                        }
                    },
                ).flow.map { pagingData -> pagingData.map { it.toUiState() } }.cachedIn(viewModelScope)
            },
            onSuccess = { movies -> updateMoviesForCountry(movies) },
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

    private fun updateMoviesForCountry(movies: Flow<PagingData<MovieUiState>>) {
        updateState {
            it.copy(
                movies = movies,
                searchByCountryContentUIState = SearchByCountryContentUIState.MOVIES_LOADED,
            )
        }
    }

    private fun onError(exception: AflamiException) {
        updateState {
            it.copy(
                isLoadingCountries = false,
                suggestedCountries = emptyList(),
                movies = emptyFlow(),
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