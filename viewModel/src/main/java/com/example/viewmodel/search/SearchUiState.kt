package com.example.viewmodel.search

data class SearchUiState(
    val recentSearchItems: List<RecentSearchItemUiState> = emptyList(),
)

data class RecentSearchItemUiState(
    val title: String = "",
)