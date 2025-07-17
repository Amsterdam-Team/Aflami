package com.example.viewmodel.search.countrySearch

interface SearchByCountryInteractionListener {
    fun onChangeSearchKeyword(keyword: String)
    fun onSelectCountry(country: CountryUiState)
    fun onClickNavigateBack()
    fun onClickRetry()
}