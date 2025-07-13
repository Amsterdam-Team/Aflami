package com.example.ui.screens.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.designsystem.R.*
import com.example.ui.R
import com.example.viewmodel.search.SearchErrorUiState

@Composable
fun getErrorMessageBySearchErrorUiState(errorUiState: SearchErrorUiState?): String {
    return when (errorUiState) {
        SearchErrorUiState.BlankQuery -> stringResource(R.string.search_error_blank_query)
        SearchErrorUiState.InvalidCharacters -> stringResource(R.string.search_error_invalid_characters)
        SearchErrorUiState.QueryTooLong -> stringResource(R.string.search_error_query_too_long)
        SearchErrorUiState.QueryTooShort -> stringResource(R.string.search_error_query_too_short)
        SearchErrorUiState.UnknownException -> stringResource(R.string.search_error_unknown)
        SearchErrorUiState.NoMoviesByKeywordFoundException -> stringResource(string.no_search_result)
        null -> ""
    }
}