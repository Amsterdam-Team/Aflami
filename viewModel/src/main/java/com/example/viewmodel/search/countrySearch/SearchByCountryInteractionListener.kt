package com.example.viewmodel.search.countrySearch

interface SearchByCountryInteractionListener {
    fun onCountryNameUpdated(countryName: String)
    fun onSelectCountry(country: CountryUiState)
}