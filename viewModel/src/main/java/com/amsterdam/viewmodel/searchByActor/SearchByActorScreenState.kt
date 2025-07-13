package com.amsterdam.viewmodel.searchByActor

import com.amsterdam.viewmodel.search.countrySearch.MovieUiState

data class SearchByActorScreenState(
    val isLoading:Boolean = false,
    val query:String="",
    val movies:List<MovieUiState> = emptyList(),
)