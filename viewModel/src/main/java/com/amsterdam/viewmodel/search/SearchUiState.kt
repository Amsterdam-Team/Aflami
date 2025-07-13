package com.amsterdam.viewmodel.search

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.BlankQueryException
import com.amsterdam.domain.exceptions.InvalidCharactersException
import com.amsterdam.domain.exceptions.NoMoviesByKeywordFoundException
import com.amsterdam.domain.exceptions.QueryTooLongException
import com.amsterdam.domain.exceptions.QueryTooShortException
import com.amsterdam.viewmodel.common.GenreItemUiState
import com.amsterdam.viewmodel.common.GenreType
import com.amsterdam.viewmodel.common.MediaItemUiState
import com.amsterdam.viewmodel.common.TabOption

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
        is NoMoviesByKeywordFoundException -> SearchErrorUiState.NoMoviesByKeywordFoundException
        else -> SearchErrorUiState.UnknownException
    }
}