package com.example.viewmodel.search.countrySearch

import androidx.lifecycle.viewModelScope
import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.CountryTooShortException
import com.example.domain.exceptions.InternetConnectionException
import com.example.domain.exceptions.NoMoviesForCountryException
import com.example.domain.exceptions.NoSuggestedCountriesException
import com.example.domain.useCase.GetMoviesByCountryUseCase
import com.example.domain.useCase.GetSuggestedCountriesUseCase
import com.example.entity.Country
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.search.mapper.toListOfUiState
import com.example.viewmodel.search.mapper.toUiState
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
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
                .filter { it.isNotBlank() }
                .flatMapLatest { query ->
                    callbackFlow {
                        sendNewEffect(SearchByCountryEffect.LoadingSuggestedCountriesEffect)
                        val job = loadCountries(query)
                        awaitClose { job.cancel() }
                    }
                }
                .collect { countries ->
                    updateSuggestedCountries(countries.toUiState())
                }
        }
    }

    private fun ProducerScope<List<Country>>.loadCountries(
        query: String
    ) = tryToExecute(
        action = { getSuggestedCountriesUseCase.invoke(query) },
        onSuccess = { result -> trySend(result).isSuccess },
        onError = {
            onError(exception = it)
            trySend(emptyList()).isSuccess
        }
    )

    override fun onCountryNameUpdated(countryName: String) {
        queryFlow.value = countryName
        updateState { it.copy(selectedCountry = countryName) }
        if (countryName.isBlank()) {
            sendNewEffect(SearchByCountryEffect.HideCountriesDropDown)
            updateState { it.copy(suggestedCountries = emptyList()) }
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