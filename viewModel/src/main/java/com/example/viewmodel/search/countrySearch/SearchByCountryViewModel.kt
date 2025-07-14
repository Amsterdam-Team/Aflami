package com.example.viewmodel.search.countrySearch

import androidx.lifecycle.viewModelScope
import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.CountryTooShortException
import com.example.domain.exceptions.InternetConnectionException
import com.example.domain.exceptions.NoMoviesForCountryException
import com.example.domain.exceptions.NoSuggestedCountriesException
import com.example.domain.useCase.GetMoviesByCountryUseCase
import com.example.domain.useCase.GetSuggestedCountriesUseCase
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.search.mapper.toListOfUiState
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

    private val queryFlow = MutableStateFlow("")

    init {
        observeQueryFlow()
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun observeQueryFlow() {
        viewModelScope.launch {
            queryFlow
                .debounce(300)
                .map(String::trim)
                .filter(String::isNotBlank)
                .collectLatest(::loadCountries)
        }
    }

    private fun loadCountries(query: String): Job {
        sendNewEffect(SearchByCountryEffect.LoadingSuggestedCountriesEffect)
        return tryToExecute(
            action = { getSuggestedCountriesUseCase(query) },
            onSuccess = { countries -> updateSuggestedCountries(countries.toUiState()) },
            onError = { onError(exception = it) }
        )
    }

    override fun onCountryNameUpdated(countryName: String) {
        queryFlow.update { countryName }
        if (countryName.isBlank()) {
            sendNewEffect(SearchByCountryEffect.HideCountriesDropDown)
            updateState { it.copy(selectedCountry = countryName, suggestedCountries = emptyList()) }
        } else {
            updateState { it.copy(selectedCountry = countryName) }
        }
    }

    override fun onSelectCountry(country: CountryUiState) {
        sendNewEffect(SearchByCountryEffect.HideCountriesDropDown)
        updateState {
            it.copy(selectedCountry = country.countryName)
        }
        getMoviesByCountry(country.countryIsoCode)
    }

    private fun getMoviesByCountry(countryIsoCode: String) {
        sendNewEffect(SearchByCountryEffect.LoadingMoviesEffect)
        tryToExecute(
            action = { getMoviesByCountryUseCase.invoke(countryIsoCode) },
            onSuccess = { movies -> updateMoviesForCountry(movies.toListOfUiState()) },
            onError = { exception -> onError(exception) }
        )
    }

    private fun updateSuggestedCountries(suggestedCountries: List<CountryUiState>) {
        updateState {
            it.copy(suggestedCountries = suggestedCountries)
        }
        if (suggestedCountries.isNotEmpty())
            sendNewEffect(SearchByCountryEffect.ShowCountriesDropDown)
    }

    private fun updateMoviesForCountry(movies: List<MovieUiState>) {
        updateState {
            it.copy(movies = movies)
        }
        sendNewEffect(SearchByCountryEffect.MoviesLoadedEffect)
    }

    private fun onError(exception: AflamiException) {
        val errorEffect = when (exception) {
            is InternetConnectionException -> SearchByCountryEffect.NoInternetConnectionEffect
            is NoSuggestedCountriesException -> SearchByCountryEffect.NoSuggestedCountriesEffect
            is NoMoviesForCountryException -> SearchByCountryEffect.NoMoviesEffect
            is CountryTooShortException -> SearchByCountryEffect.CountryTooShortEffect
            else -> SearchByCountryEffect.UnknownErrorEffect
        }
        sendNewEffect(errorEffect)
    }
}