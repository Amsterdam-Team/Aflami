package com.example.viewmodel.search.mapper

import com.example.entity.category.MovieGenre
import com.example.entity.category.TvShowGenre
import com.example.viewmodel.shared.uiStates.MovieGenreItemUiState
import com.example.viewmodel.shared.uiStates.TvGenreItemUiState
import com.example.viewmodel.shared.Selectable

fun List<MovieGenreItemUiState>.selectByMovieGenre(movieGenre: MovieGenre): List<MovieGenreItemUiState> {
    return this.map { movies ->
        movies.copy(
            selectableMovieGenre = Selectable(
                item = movies.selectableMovieGenre.item,
                isSelected = movies.selectableMovieGenre.item == movieGenre
            )
        )
    }
}

fun List<TvGenreItemUiState>.selectByTvGenre(tvGenre: TvShowGenre): List<TvGenreItemUiState> {
    return this.map { tvShows ->
        tvShows.copy(
            selectableTvShowGenre = Selectable(
                item = tvShows.selectableTvShowGenre.item,
                isSelected = tvShows.selectableTvShowGenre.item == tvGenre
            )
        )
    }
}

fun List<MovieGenreItemUiState>.getSelectedGenreType(): MovieGenre {
    return this.find { it.selectableMovieGenre.isSelected }?.selectableMovieGenre?.item
        ?: MovieGenre.ALL
}

fun List<TvGenreItemUiState>.getSelectedGenreType(): TvShowGenre {
    return this.find { it.selectableTvShowGenre.isSelected }?.selectableTvShowGenre?.item
        ?: TvShowGenre.ALL
}