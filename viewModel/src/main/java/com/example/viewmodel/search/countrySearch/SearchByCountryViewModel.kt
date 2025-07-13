package com.example.viewmodel.search.countrySearch

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

class SearchByCountryViewModel(
    private val getSuggestedCountriesUseCase: GetSuggestedCountriesUseCase,
    private val getMoviesByCountryUseCase: GetMoviesByCountryUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<SearchByCountryScreenState, SearchByCountryEffect>(
    SearchByCountryScreenState(),
    dispatcherProvider
) {

    init {
        sendNewEffect(SearchByCountryEffect.InitialEffect)
    }

    fun onCountryNameUpdated(countryName: String) {
        updateState {
            it.copy(selectedCountry = countryName)
        }
        if (countryName.isNotEmpty()) {
            getSuggestedCountries(countryName)
            return
        }
        sendNewEffect(SearchByCountryEffect.HideCountriesDropDown)
    }

    fun onSelectCountry(country: CountryUiState) {
        updateState {
            it.copy(selectedCountry = country.countryName)
        }
        sendNewEffect(SearchByCountryEffect.HideCountriesDropDown)
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

    private fun getSuggestedCountries(countryName: String) {
        sendNewEffect(SearchByCountryEffect.LoadingSuggestedCountriesEffect)
        tryToExecute(
            action = { getSuggestedCountriesUseCase.invoke(countryName) },
            onSuccess = { suggestedCountries -> updateSuggestedCountries(suggestedCountries.toUiState()) },
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