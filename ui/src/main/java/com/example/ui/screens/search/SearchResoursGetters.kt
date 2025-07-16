package com.example.ui.screens.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.designsystem.R
import com.example.viewmodel.search.globalSearch.SearchErrorState

@Composable
fun getErrorMessageBySearchErrorUiState(errorUiState: SearchErrorState?): String {
    return when (errorUiState) {
        SearchErrorState.BlankQuery -> stringResource(R.string.search_error_blank_query)
        SearchErrorState.QueryTooLong -> stringResource(R.string.search_error_query_too_long)
        SearchErrorState.UnknownException -> stringResource(R.string.search_error_unknown)
        SearchErrorState.NoResultFoundException -> stringResource(R.string.no_search_result)
        SearchErrorState.NoNetworkConnection -> stringResource(R.string.offline_message)
        null -> ""
    }
}