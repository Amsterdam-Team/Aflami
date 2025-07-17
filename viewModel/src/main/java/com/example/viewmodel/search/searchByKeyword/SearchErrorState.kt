package com.example.viewmodel.search.searchByKeyword

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NetworkException

sealed interface SearchErrorState {
    object UnknownException : SearchErrorState
    object NoNetworkConnection : SearchErrorState
}

internal fun mapToSearchUiState(aflamiException: AflamiException): SearchErrorState {
    return when (aflamiException) {
        is NetworkException -> SearchErrorState.NoNetworkConnection
        else -> SearchErrorState.UnknownException
    }
}