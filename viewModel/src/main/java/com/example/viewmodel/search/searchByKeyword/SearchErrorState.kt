package com.example.viewmodel.search.searchByKeyword

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NetworkException
import com.example.domain.exceptions.QueryTooLongException

sealed interface SearchErrorState {
    object QueryTooLong : SearchErrorState
    object UnknownException : SearchErrorState
    object NoResultFoundException : SearchErrorState
    object NoNetworkConnection : SearchErrorState
}

internal fun mapToSearchUiState(aflamiException: AflamiException): SearchErrorState {
    return when (aflamiException) {
        is QueryTooLongException -> SearchErrorState.QueryTooLong
        is NetworkException -> SearchErrorState.NoNetworkConnection
        else -> SearchErrorState.UnknownException
    }
}