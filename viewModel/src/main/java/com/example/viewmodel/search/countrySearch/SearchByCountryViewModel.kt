package com.example.viewmodel.search.countrySearch

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.CountryTooShortException
import com.example.domain.exceptions.InternetConnectionException
import com.example.domain.exceptions.NoMoviesForCountryException
import com.example.domain.exceptions.NoSuggestedCountriesException
import com.example.domain.usecase.GetMoviesByCountryUseCase
import com.example.domain.usecase.GetSuggestedCountriesUseCase
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.search.mapper.toListOfUiState
import com.example.viewmodel.search.mapper.toUiState

class SearchByCountryViewModel(
    private val suggestedCountriesUseCase: GetSuggestedCountriesUseCase,
    private val moviesByCountryUseCase: GetMoviesByCountryUseCase,
) : BaseViewModel<SearchByCountryScreenState, SearchByCountryEffect>(
    SearchByCountryScreenState()
) {

    init {
        sendNewEffect(SearchByCountryEffect.InitialEffect)
    }

    fun onCountryNameUpdated(countryName: String) {
        updateState {
            it.copy(selectedCountry = countryName)
        }
        getSuggestedCountries(countryName)
    }

    fun onSelectCountryName(countryName: String) {
        val countryIsoCode =
            state.value.suggestedCountries.find { it.countryName == countryName }?.countryIsoCode
                ?: ""
        getMoviesByCountry(countryIsoCode)
    }

    private fun getMoviesByCountry(countryIsoCode: String) {
        sendNewEffect(SearchByCountryEffect.LoadingMoviesEffect)
        tryToExecute(
            action = { moviesByCountryUseCase.invoke(countryIsoCode) },
            onSuccess = { movies -> updateMoviesForCountry(movies.toListOfUiState()) },
            onError = { exception -> onError(exception) }
        )
    }

    private fun getSuggestedCountries(countryName: String) {
        sendNewEffect(SearchByCountryEffect.LoadingSuggestedCountriesEffect)
        tryToExecute(
            action = { suggestedCountriesUseCase.invoke(countryName) },
            onSuccess = { suggestedCountries -> updateSuggestedCountries(suggestedCountries.toUiState()) },
            onError = { exception -> onError(exception) }
        )
    }

    private fun updateSuggestedCountries(suggestedCountries: List<CountryUiState>) {
        updateState {
            it.copy(suggestedCountries = suggestedCountries)
        }
        sendNewEffect(SearchByCountryEffect.SuggestedCountriesLoadedEffect)
    }

    private fun updateMoviesForCountry(movies: List<MovieUiState>) {
        updateState {
            it.copy(movies = movies)
        }
        sendNewEffect(SearchByCountryEffect.MoviesLoadedEffect)
    }

    private fun onError(exception: AflamiException) {
        when (exception) {
            is InternetConnectionException -> SearchByCountryEffect.NoInternetConnectionEffect
            is NoSuggestedCountriesException -> SearchByCountryEffect.NoSuggestedCountriesEffect
            is NoMoviesForCountryException -> SearchByCountryEffect.NoMoviesEffect
            is CountryTooShortException -> SearchByCountryEffect.CountryTooShortEffect
            else -> {}
        }
    }
}