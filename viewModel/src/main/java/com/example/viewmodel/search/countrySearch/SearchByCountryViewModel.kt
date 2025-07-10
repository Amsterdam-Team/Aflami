package com.example.viewmodel.search.countrySearch

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.InternetConnectionException
import com.example.domain.exceptions.NoMoviesForCountryException
import com.example.domain.exceptions.NoSuggestedCountries
import com.example.domain.usecase.GetMoviesByCountryUseCase
import com.example.domain.usecase.GetSuggestedCountriesUseCase
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.search.mapper.toListOfString
import com.example.viewmodel.search.mapper.toListOfUiState

class SearchByCountryViewModel(
    private val suggestedCountriesUseCase: GetSuggestedCountriesUseCase,
    private val moviesByCountryUseCase: GetMoviesByCountryUseCase,
) : BaseViewModel<SearchByCountryUiState, SearchByCountryEffect>(
    SearchByCountryUiState()
) {
    fun onCountryNameUpdated(countryName: String) {
        updateState {
            it.copy(selectedCountry = countryName)
        }
        getSuggestedCountries(countryName)
    }

    fun getMoviesByCountry(countryName: String) {
        sendNewEffect(SearchByCountryEffect.LoadingMoviesEffect)
        tryToExecute(
            action = { moviesByCountryUseCase.invoke(countryName) },
            onSuccess = { movies -> updateMoviesForCountry(movies.toListOfUiState()) },
            onError = { exception -> onError(exception) }
        )
    }

    private fun getSuggestedCountries(countryName: String) {
        sendNewEffect(SearchByCountryEffect.LoadingSuggestedCountriesEffect)
        tryToExecute(
            action = { suggestedCountriesUseCase.invoke(countryName) },
            onSuccess = { suggestedCountries -> updateSuggestedCountries(suggestedCountries.toListOfString()) },
            onError = { exception -> onError(exception) }
        )
    }

    private fun updateSuggestedCountries(suggestedCountries: List<String>) {
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
            is NoSuggestedCountries -> SearchByCountryEffect.NoSuggestedCountriesEffect
            is NoMoviesForCountryException -> SearchByCountryEffect.NoMoviesEffect
            else -> {}
        }
    }
}