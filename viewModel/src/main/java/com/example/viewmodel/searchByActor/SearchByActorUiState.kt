package com.example.viewmodel.searchByActor

import android.graphics.Movie

data class SearchByActorUiState(
    val isLoading:Boolean = false,
    val isError:Boolean = false,
    val query:String="",
    val result:List<Movie> = emptyList(),
)