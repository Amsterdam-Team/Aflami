package com.amsterdam.viewmodel.search.countrySearch

sealed interface CountrySearchEffect {
    data object NavigateBack : CountrySearchEffect
    data object NavigateToMovieDetails : CountrySearchEffect
}