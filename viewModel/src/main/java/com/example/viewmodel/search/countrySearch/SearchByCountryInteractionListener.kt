package com.example.viewmodel.search.countrySearch

interface SearchByCountryInteractionListener {
    fun onKeywordValueChanged(keyword: String)
    fun onCountrySelected(country: CountryUiState)
    fun onRetryQuestClicked()
    fun onMovieClicked(movieId : Long)
}