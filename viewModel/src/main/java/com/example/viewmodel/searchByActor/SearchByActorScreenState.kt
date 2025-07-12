package com.example.viewmodel.searchByActor

import com.example.viewmodel.search.countrySearch.MovieUiState

data class SearchByActorScreenState(
    val isLoading:Boolean = false,
    val query:String="",
    val movies:List<MovieUiState> = emptyList(),
)