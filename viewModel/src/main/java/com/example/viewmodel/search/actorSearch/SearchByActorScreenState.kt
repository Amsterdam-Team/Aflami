package com.example.viewmodel.search.actorSearch

import com.example.viewmodel.search.countrySearch.MovieUiState

data class SearchByActorScreenState(
    val isLoading:Boolean = false,
    val query:String="",
    val movies:List<MovieUiState> = emptyList(),
    val selectedMovieId : Long = 0,
    val isNetworkError : Boolean = false
)