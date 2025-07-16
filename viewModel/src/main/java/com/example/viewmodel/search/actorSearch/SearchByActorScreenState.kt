package com.example.viewmodel.search.actorSearch

import com.example.viewmodel.search.countrySearch.MovieUiState
import com.example.viewmodel.search.globalSearch.SearchErrorState

data class SearchByActorScreenState(
    val isLoading: Boolean = false,
    val keyword: String = "",
    val movies: List<MovieUiState> = emptyList(),
    val noInternetException: Boolean = false,
    val errorUiState: ActorSearchErrorState? = null,
)