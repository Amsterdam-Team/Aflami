package com.example.viewmodel.search.countrySearch

interface SearchByCountryInteractionListener {
    fun onKeywordValueChanged(keyword: String)
    fun onCountrySelected(country: CountryUiState)
    fun onNavigateBackClicked()
    fun onRetryRequestClicked()
    fun onMovieClicked(movieId : Long)
}