package com.example.viewmodel.searchByActor

import com.example.entity.Movie

data class SearchByActorUiState(
    val isLoading:Boolean = false,
    val query:String="",
    val movies:List<Movie> = emptyList(),
)