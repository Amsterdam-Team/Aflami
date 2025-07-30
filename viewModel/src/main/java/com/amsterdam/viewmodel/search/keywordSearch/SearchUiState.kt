package com.amsterdam.viewmodel.search.keywordSearch

import androidx.paging.PagingData
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.viewmodel.shared.Selectable
import com.amsterdam.viewmodel.shared.defaultMovieGenres
import com.amsterdam.viewmodel.shared.uiStates.MovieGenreItemUiState
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.shared.uiStates.TvGenreItemUiState
import com.amsterdam.viewmodel.shared.uiStates.TvShowItemUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class SearchUiState(
    val keyword: String = "",
    val recentSearches: List<String> = emptyList(),
    val selectedTabOption: TabOption = TabOption.MOVIES,
    val movies: Flow<PagingData<MovieItemUiState>> = emptyFlow(),
    val tvShows: Flow<PagingData<TvShowItemUiState>> = emptyFlow(),
    val isDialogVisible: Boolean = false,
    val movieFilterItemUiState: FilterItemUiState = FilterItemUiState(),
    val tvShowFilterItemUiState: FilterItemUiState = FilterItemUiState(),
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
        get() = selectedStarIndex > 0 || selectableMovieGenres.any { it.selectableMovieGenre.isSelected } || selectableTvShowGenres.any { it.selectableTvShowGenre.isSelected }

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
    data object NoNetworkConnection : SearchErrorState
    data object UnknownError : SearchErrorState
    companion object {
        fun toSearchErrorState(exception: Throwable): SearchErrorState =
            when (exception) {
                is NetworkException -> NoNetworkConnection
                else -> UnknownError
            }
    }
}