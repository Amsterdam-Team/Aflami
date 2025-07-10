package com.example.ui.screen.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.viewmodel.search.SearchErrorUiState
import com.example.ui.R

@Composable
fun getErrorMessageBySearchErrorUiState(errorUiState: SearchErrorUiState?): String{
    return when (errorUiState) {
        SearchErrorUiState.BlankQuery -> stringResource(R.string.search_error_blank_query)
        SearchErrorUiState.InvalidCharacters -> stringResource(R.string.search_error_invalid_characters)
        SearchErrorUiState.QueryTooLong -> stringResource(R.string.search_error_query_too_long)
        SearchErrorUiState.QueryTooShort -> stringResource(R.string.search_error_query_too_short)
        null -> ""
    }
}