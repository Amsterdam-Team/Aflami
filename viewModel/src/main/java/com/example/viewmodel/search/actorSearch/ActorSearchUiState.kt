package com.example.viewmodel.search.actorSearch

import com.example.viewmodel.shared.uiStates.MovieItemUiState


data class ActorSearchUiState(
    val isLoading:Boolean = false,
    val keyword:String="",
    val movies:List<MovieItemUiState> = emptyList(),
    val error : SearchByActorError? = null
){
    sealed class SearchByActorError{
        data object NetworkError : SearchByActorError()
    }
}

