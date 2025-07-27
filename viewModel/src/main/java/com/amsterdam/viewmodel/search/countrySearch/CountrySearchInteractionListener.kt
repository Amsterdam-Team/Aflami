package com.amsterdam.viewmodel.search.countrySearch

import androidx.paging.CombinedLoadStates

interface CountrySearchInteractionListener {
    fun onChangeSearchKeyword(keyword: String)
    fun onSelectCountry(country: CountryItemUiState)
    fun onClickNavigateBack()
    fun onClickRetry()
    fun onClickMovieCard(movieId: Long)
    fun onPagingLoadStateChanged(loadStates: CombinedLoadStates)
}