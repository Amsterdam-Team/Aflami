package com.example.viewmodel.search.countrySearch

sealed interface SearchByCountryEffect {
    data object NavigateBack : SearchByCountryEffect
}