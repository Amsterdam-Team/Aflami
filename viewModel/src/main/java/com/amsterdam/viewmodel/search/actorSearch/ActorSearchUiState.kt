package com.amsterdam.viewmodel.search.actorSearch

import androidx.paging.PagingData
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ActorSearchUiState(
    val isLoading:Boolean = false,
    val keyword:String="",
    val movies: Flow<PagingData<MovieItemUiState>> = emptyFlow(),
    val error : ActorSearchErrorState? = null
)

sealed interface ActorSearchErrorState {
    data object NoNetworkConnection : ActorSearchErrorState
    data object UnknownError : ActorSearchErrorState
    companion object {
        fun toActorSearchErrorState(exception: Throwable): ActorSearchErrorState =
            when (exception) {
                is NetworkException -> NoNetworkConnection
                else -> UnknownError
            }
    }
}

