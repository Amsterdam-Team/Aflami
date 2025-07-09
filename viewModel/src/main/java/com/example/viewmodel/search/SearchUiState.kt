package com.example.viewmodel.search

data class SearchUiState(
    val isSearchQueryEmpty: Boolean = false,
    val recentSearchItems: List<RecentSearchItemUiState> = emptyList(),
)

data class RecentSearchItemUiState(
    val title: String = "",
)