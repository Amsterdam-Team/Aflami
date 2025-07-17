package com.example.viewmodel.search.countrySearch

interface CountrySearchInteractionListener {
    fun onChangeSearchKeyword(keyword: String)
    fun onSelectCountry(country: CountryItemUiState)
    fun onClickNavigateBack()
    fun onClickRetry()
}