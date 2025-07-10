package com.example.viewmodel.searchByActor

import com.example.entity.Movie


data class SearchByActorUiState(
    val isLoading:Boolean = false,
    val isError:Boolean = false,
    val query:String="",
    val result:List<Movie> = emptyList(),
)