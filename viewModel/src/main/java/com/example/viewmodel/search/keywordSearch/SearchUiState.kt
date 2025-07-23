package com.example.viewmodel.search.keywordSearch

import androidx.paging.PagingData
import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NetworkException
import com.example.entity.category.TvShowGenre
import com.example.viewmodel.shared.Selectable
import com.example.viewmodel.shared.defaultMovieGenres
import com.example.viewmodel.shared.uiStates.MovieGenreItemUiState
import com.example.viewmodel.shared.uiStates.MovieItemUiState
import com.example.viewmodel.shared.uiStates.TvGenreItemUiState
import com.example.viewmodel.shared.uiStates.TvShowItemUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class SearchUiState(
    val keyword: String = "",
    val recentSearches: List<String> = emptyList(),
    val selectedTabOption: TabOption = TabOption.MOVIES,
    val movies: Flow<PagingData<MovieItemUiState>> = emptyFlow(),
    val tvShows: Flow<PagingData<TvShowItemUiState>> = emptyFlow(),
    val isDialogVisible: Boolean = false,
    val filterItemUiState: FilterItemUiState = FilterItemUiState(),
    val isLoading: Boolean = false,
    val errorUiState: SearchErrorState? = null,
)

enum class TabOption(
    val index: Int,
) {
    MOVIES(index = 0),
    TV_SHOWS(index = 1),
}

data class FilterItemUiState(
    val selectedStarIndex: Int = 0,
    val selectableMovieGenres: List<MovieGenreItemUiState> = defaultMovieGenres,
    val selectableTvShowGenres: List<TvGenreItemUiState> = defaultTvShowGenres,
    val isLoading: Boolean = false,
) {
    val hasFilterData: Boolean
        get() = selectedStarIndex > 0 || selectableMovieGenres.any { it.selectableMovieGenre.isSelected }

    private companion object {
        val defaultTvShowGenres =
            TvShowGenre.entries.toTypedArray().mapIndexed { index, category ->
                TvGenreItemUiState(
                    selectableTvShowGenre =
                        Selectable(
                            item = category,
                            isSelected = index == 0,
                        ),
                )
            }
    }
}

sealed interface SearchErrorState {
    object NoNetworkConnection : SearchErrorState

    object UnknownError : SearchErrorState

    companion object {
        fun toSearchErrorState(exception: AflamiException): SearchErrorState =
            when (exception) {
                is NetworkException -> NoNetworkConnection
                else -> UnknownError
            }
    }
}
