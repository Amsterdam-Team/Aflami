package com.example.viewmodel.search.globalSearch

import com.example.viewmodel.common.MediaItemUiState
import com.example.viewmodel.search.globalSearch.genre.MovieGenreItemUiState
import com.example.viewmodel.search.globalSearch.genre.Selectable
import com.example.viewmodel.common.TabOption
import com.example.viewmodel.search.globalSearch.genre.TvGenreItemUiState
import com.example.viewmodel.search.globalSearch.genre.MovieGenre
import com.example.viewmodel.search.globalSearch.genre.TvShowGenre

data class SearchUiState(
    val query: String = "",
    val recentSearches: List<String> = emptyList(),
    val selectedTabOption: TabOption = TabOption.MOVIES,
    val movies: List<MediaItemUiState> = emptyList(),
    val tvShows: List<MediaItemUiState> = emptyList(),
    val isDialogVisible: Boolean = false,
    val filterItemUiState: FilterItemUiState = FilterItemUiState(),
    val isLoading: Boolean = false,
    val errorUiState: SearchErrorState? = null,
)

data class FilterItemUiState(
    val selectedStarIndex: Int = 0,
    val selectableMovieGenres: List<MovieGenreItemUiState> = defaultMovieGenres,
    val selectableTvShowGenres: List<TvGenreItemUiState> = defaultTvShowGenres,
    val isLoading: Boolean = false,
) {
    val hasFilterData: Boolean
        get() = selectedStarIndex > 0 || selectableMovieGenres.any { it.selectableMovieGenre.isSelected }

    private companion object {
        val defaultMovieGenres =
            MovieGenre.entries.toTypedArray().mapIndexed { index, category ->
                MovieGenreItemUiState(
                    selectableMovieGenre = Selectable(
                        type = category,
                        isSelected = index == 0
                    )
                )
            }

        val defaultTvShowGenres =
            TvShowGenre.entries.toTypedArray().mapIndexed { index, category ->
                TvGenreItemUiState(
                    selectableTvShowGenre = Selectable(
                        type = category,
                        isSelected = index == 0
                    )
                )
            }
    }
}