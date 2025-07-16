package com.example.viewmodel.search.searchByKeyword.genre

import com.example.entity.category.MovieGenre
import com.example.entity.category.TvShowGenre

data class MovieGenreItemUiState(
    val selectableMovieGenre: Selectable<MovieGenre> = Selectable(
        type = MovieGenre.ALL,
        isSelected = false
    )
)

data class TvGenreItemUiState(
    val selectableTvShowGenre: Selectable<TvShowGenre> = Selectable(
        type = TvShowGenre.ALL,
        isSelected = false
    )
)

data class Selectable<T>(
    val isSelected: Boolean = true,
    val type: T
)