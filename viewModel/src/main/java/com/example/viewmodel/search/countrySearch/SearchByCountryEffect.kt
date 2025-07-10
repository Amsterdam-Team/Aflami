package com.example.viewmodel.search.countrySearch

sealed interface SearchByCountryEffect {
    data object NoInternetConnectionEffect: SearchByCountryEffect
    data object NoDataFoundEffect: SearchByCountryEffect
    data object LoadingDataEffect: SearchByCountryEffect
}