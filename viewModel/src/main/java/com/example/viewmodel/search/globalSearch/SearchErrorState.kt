package com.example.viewmodel.search.globalSearch

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.BlankQueryException
import com.example.domain.exceptions.NetworkException
import com.example.domain.exceptions.NoSearchByKeywordResultFoundException
import com.example.domain.exceptions.QueryTooLongException

sealed interface SearchErrorState {
    object QueryTooLong : SearchErrorState
    object BlankQuery : SearchErrorState
    object UnknownException : SearchErrorState
    object NoResultFoundException : SearchErrorState
    object NoNetworkConnection : SearchErrorState
}

fun mapToSearchUiState(aflamiException: AflamiException): SearchErrorState {
    return when (aflamiException) {
        is QueryTooLongException -> SearchErrorState.QueryTooLong
        is BlankQueryException -> SearchErrorState.BlankQuery
        is NoSearchByKeywordResultFoundException -> SearchErrorState.NoResultFoundException
        is NetworkException -> SearchErrorState.NoNetworkConnection
        else -> SearchErrorState.UnknownException
    }
}