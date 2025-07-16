package com.example.viewmodel.search.mapper

import com.example.entity.category.MovieGenre
import com.example.entity.category.TvShowGenre
import com.example.viewmodel.search.searchByKeyword.genre.MovieGenreItemUiState
import com.example.viewmodel.search.searchByKeyword.genre.Selectable
import com.example.viewmodel.search.searchByKeyword.genre.TvGenreItemUiState


fun List<MovieGenreItemUiState>.selectByMovieGenre(movieGenre: MovieGenre): List<MovieGenreItemUiState> {
    return this.map { movies ->
        movies.copy(
            selectableMovieGenre = Selectable(
                type = movies.selectableMovieGenre.type,
                isSelected = movies.selectableMovieGenre.type == movieGenre
            )
        )
    }
}

fun List<TvGenreItemUiState>.selectByTvGenre(tvGenre: TvShowGenre): List<TvGenreItemUiState> {
    return this.map { tvShows ->
        tvShows.copy(
            selectableTvShowGenre = Selectable(
                type = tvShows.selectableTvShowGenre.type,
                isSelected = tvShows.selectableTvShowGenre.type == tvGenre
            )
        )
    }
}


fun List<MovieGenreItemUiState>.getSelectedGenreType(): MovieGenre {
    return this.find { it.selectableMovieGenre.isSelected }?.selectableMovieGenre?.type
        ?: MovieGenre.ALL
}

fun List<TvGenreItemUiState>.getSelectedGenreType(): TvShowGenre {
    return this.find { it.selectableTvShowGenre.isSelected }?.selectableTvShowGenre?.type
        ?: TvShowGenre.ALL

}