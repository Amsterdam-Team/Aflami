package com.example.viewmodel.search.actorSearch

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.BlankQueryException
import com.example.domain.exceptions.NetworkException
import com.example.domain.exceptions.NoSearchByKeywordResultFoundException
import com.example.domain.exceptions.QueryTooLongException

sealed interface ActorSearchErrorState {
    object NoResultFoundException : ActorSearchErrorState
    object NoNetworkConnection : ActorSearchErrorState
}

fun mapToActorSearchUiState(aflamiException: AflamiException): ActorSearchErrorState {
    return when (aflamiException) {

        is NoSearchByKeywordResultFoundException -> ActorSearchErrorState.NoResultFoundException
        is NetworkException -> ActorSearchErrorState.NoNetworkConnection
        else -> ActorSearchErrorState.NoResultFoundException
    }
}