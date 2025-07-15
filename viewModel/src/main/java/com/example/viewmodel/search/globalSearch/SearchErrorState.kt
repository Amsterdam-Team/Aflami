package com.example.viewmodel.search.globalSearch

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.BlankQueryException
import com.example.domain.exceptions.InvalidCharactersException
import com.example.domain.exceptions.NetworkException
import com.example.domain.exceptions.NoSearchByKeywordResultFoundException
import com.example.domain.exceptions.QueryTooLongException
import com.example.domain.exceptions.QueryTooShortException

sealed interface SearchErrorState {
    object QueryTooShort : SearchErrorState
    object QueryTooLong : SearchErrorState
    object InvalidCharacters : SearchErrorState
    object BlankQuery : SearchErrorState
    object UnknownException : SearchErrorState
    object NoMoviesByKeywordFoundException : SearchErrorState
    object NoNetworkConnection : SearchErrorState
}

fun mapToSearchUiState(aflamiException: AflamiException): SearchErrorState {
    return when (aflamiException) {
        is QueryTooShortException -> SearchErrorState.QueryTooShort
        is QueryTooLongException -> SearchErrorState.QueryTooLong
        is InvalidCharactersException -> SearchErrorState.InvalidCharacters
        is BlankQueryException -> SearchErrorState.BlankQuery
        is NoSearchByKeywordResultFoundException -> SearchErrorState.NoMoviesByKeywordFoundException
        is NetworkException -> SearchErrorState.NoNetworkConnection
        else -> SearchErrorState.UnknownException
    }
}