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
    val filterItemUiState: FilterItemUiState = FilterItemUiState()
)

data class FilterItemUiState(
    val selectedStarIndex: Int = 10,
    val genreUiStates: List<GenreItemUiState> = GenreType.toGenreItemsUiState()
)