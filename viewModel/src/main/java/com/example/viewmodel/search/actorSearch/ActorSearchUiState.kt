package com.example.viewmodel.search.actorSearch

import com.example.viewmodel.search.countrySearch.MovieUiState

data class ActorSearchUiState(
    val isLoading:Boolean = false,
    val keyword:String="",
    val movies:List<MovieUiState> = emptyList(),
    val error : SearchByActorError? = null
){
    sealed class SearchByActorError{
        data object NetworkError : SearchByActorError()
    }
}

