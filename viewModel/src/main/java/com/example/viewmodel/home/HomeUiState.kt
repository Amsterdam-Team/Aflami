package com.example.viewmodel.home

import com.example.entity.category.MovieGenre
import com.example.viewmodel.shared.defaultMovieGenres
import com.example.viewmodel.shared.uiStates.MovieGenreItemUiState
import com.example.viewmodel.shared.uiStates.MovieItemUiState
import com.example.viewmodel.shared.uiStates.media.MediaItemUiState
import com.example.viewmodel.shared.uiStates.media.MediaType

data class HomeUiState(
    val popularMediaItems: List<PopularMediaItemUiState> = emptyList(),
    val topRatedMediaItems: List<MediaItemUiState> = emptyList(),
    val upcomingMovies: List<MovieItemUiState> = emptyList(),
    val upcomingMovieGenres: List<MovieGenreItemUiState> = defaultMovieGenres,
    val isLoading: Boolean = false,
    val error: HomeError? = null
) {
    data class PopularMediaItemUiState(
        val name: String = "",
        val rating: String = "",
        val posterUrl: String = "",
        val type: MediaType = MediaType.MOVIE
    )

    sealed class HomeError {
        data object NetworkError : HomeError()
    }

    fun getSelectedUpcomingMovieGenre(): MovieGenre {
        return upcomingMovieGenres
            .firstOrNull { it.selectableMovieGenre.isSelected }
            ?.selectableMovieGenre?.item ?: MovieGenre.ALL
    }
}
