package com.example.viewmodel.search.countrySearch

sealed interface SearchByCountryEffect {
    data object NoInternetConnectionEffect: SearchByCountryEffect
    data object NoSuggestedCountriesEffect: SearchByCountryEffect
    data object NoMoviesEffect: SearchByCountryEffect
    data object LoadingMoviesEffect: SearchByCountryEffect
    data object HideCountriesDropDown: SearchByCountryEffect
    data object ShowCountriesDropDown: SearchByCountryEffect
    data object LoadingSuggestedCountriesEffect: SearchByCountryEffect
    data object MoviesLoadedEffect: SearchByCountryEffect
    data object InitialEffect: SearchByCountryEffect
    data object CountryTooShortEffect: SearchByCountryEffect
    data object UnknownErrorEffect : SearchByCountryEffect
}