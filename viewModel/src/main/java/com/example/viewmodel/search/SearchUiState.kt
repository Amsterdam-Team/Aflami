package com.example.viewmodel.search

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.BlankQueryException
import com.example.domain.exceptions.InvalidCharactersException
import com.example.domain.exceptions.NoSearchByKeywordResultFoundException
import com.example.domain.exceptions.QueryTooLongException
import com.example.domain.exceptions.QueryTooShortException
import com.example.viewmodel.common.GenreItemUiState
import com.example.viewmodel.common.GenreType
import com.example.viewmodel.common.MediaItemUiState
import com.example.viewmodel.common.TabOption

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
    val genreItemUiStates: List<GenreItemUiState> = defaultGenreItemsUiState,
    val isLoading: Boolean = false,
){
    val hasFilterData: Boolean
        get() = selectedStarIndex > 0 || genreItemUiStates.any { it.isSelected }

    companion object {
        val defaultGenreItemsUiState = GenreType.entries.toTypedArray().mapIndexed { index, genreType ->
            GenreItemUiState(
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
    object NoMoviesByKeywordFoundException : SearchErrorUiState
}

fun mapToSearchUiState(aflamiException: AflamiException): SearchErrorUiState {
    return when (aflamiException) {
        is QueryTooShortException -> SearchErrorUiState.QueryTooShort
        is QueryTooLongException -> SearchErrorUiState.QueryTooLong
        is InvalidCharactersException -> SearchErrorUiState.InvalidCharacters
        is BlankQueryException -> SearchErrorUiState.BlankQuery
        is NoSearchByKeywordResultFoundException -> SearchErrorUiState.NoMoviesByKeywordFoundException
        else -> SearchErrorUiState.UnknownException
    }
}