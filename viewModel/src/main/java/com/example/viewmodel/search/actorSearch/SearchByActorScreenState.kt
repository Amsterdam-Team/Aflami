package com.example.viewmodel.search.actorSearch

import com.example.viewmodel.search.countrySearch.MovieUiState

data class SearchByActorScreenState(
    val isLoading:Boolean = false,
    val keyword:String="",
    val movies:List<MovieUiState> = emptyList(),
    val noInternetException:Boolean =false
)