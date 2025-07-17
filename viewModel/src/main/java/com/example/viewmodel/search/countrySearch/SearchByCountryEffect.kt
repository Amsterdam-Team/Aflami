package com.example.viewmodel.search.countrySearch

sealed interface SearchByCountryEffect {
    object NavigateToMovieDetails : SearchByCountryEffect
    data object NavigateBack : SearchByCountryEffect
}