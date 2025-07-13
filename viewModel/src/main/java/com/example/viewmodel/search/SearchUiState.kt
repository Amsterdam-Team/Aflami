package com.example.viewmodel.search

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.BlankQueryException
import com.example.domain.exceptions.InvalidCharactersException
import com.example.domain.exceptions.QueryTooLongException
import com.example.domain.exceptions.QueryTooShortException
import com.example.viewmodel.common.CategoryItemUiState
import com.example.viewmodel.common.MediaItemUiState
import com.example.viewmodel.common.TabOption
import com.example.viewmodel.search.mapper.CategoryType

data class SearchUiState(
    val query: String = "",
    val recentSearches: List<String> = emptyList(),
    val selectedTabOption: TabOption = TabOption.MOVIES,
    val movies: List<MediaItemUiState> = emptyList(),
    val tvShows: List<MediaItemUiState> = emptyList(),
    val isDialogVisible: Boolean = false,
    val filterItemUiState: FilterItemUiState = FilterItemUiState(),
    val isLoading: Boolean = false,
    val errorUiState: SearchErrorUiState? = null,
)

data class FilterItemUiState(
    val selectedStarIndex: Int = 0,
    val genreItemUiStates: List<CategoryItemUiState> = defaultGenreItemsUiState,
){
    val hasFilterData: Boolean
        get() = selectedStarIndex > 0 || genreItemUiStates.any { it.isSelected }

    companion object {
        val defaultGenreItemsUiState = CategoryType.entries.toTypedArray().mapIndexed { index, genreType ->
            CategoryItemUiState(
                type = genreType,
                isSelected = index == 0
            )
        }
    }
}

sealed interface SearchErrorUiState {
    object QueryTooShort : SearchErrorUiState
    object QueryTooLong : SearchErrorUiState
    object InvalidCharacters : SearchErrorUiState
    object BlankQuery : SearchErrorUiState
    object UnknownException : SearchErrorUiState
}

fun mapToSearchUiState(aflamiException: AflamiException): SearchErrorUiState {
    return when (aflamiException) {
        is QueryTooShortException -> SearchErrorUiState.QueryTooShort
        is QueryTooLongException -> SearchErrorUiState.QueryTooLong
        is InvalidCharactersException -> SearchErrorUiState.InvalidCharacters
        is BlankQueryException -> SearchErrorUiState.BlankQuery
        else -> SearchErrorUiState.UnknownException
    }
}