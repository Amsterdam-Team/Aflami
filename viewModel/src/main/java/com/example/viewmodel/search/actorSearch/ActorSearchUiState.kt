package com.example.viewmodel.search.actorSearch

import androidx.paging.PagingData
import com.example.viewmodel.shared.uiStates.MovieItemUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ActorSearchUiState(
    val isLoading:Boolean = false,
    val keyword:String="",
    val movies: Flow<PagingData<MovieItemUiState>> = emptyFlow(),
    val error : SearchByActorError? = null
){
    sealed class SearchByActorError{
        data object NetworkError : SearchByActorError()
    }
}

