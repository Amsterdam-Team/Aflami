package com.example.viewmodel.search

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
    val selectedStarIndex: Int = 10,
    val genreUiStates: List<GenreItemUiState> = GenreType.toGenreItemsUiState()
)

sealed interface SearchErrorUiState {
    object QueryTooShort : SearchErrorUiState
    object QueryTooLong : SearchErrorUiState
    object InvalidCharacters : SearchErrorUiState
    object BlankQuery : SearchErrorUiState
}