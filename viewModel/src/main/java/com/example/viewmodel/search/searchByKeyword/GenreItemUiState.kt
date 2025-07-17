package com.example.viewmodel.search.searchByKeyword

import com.example.entity.category.MovieGenre
import com.example.entity.category.TvShowGenre
import com.example.viewmodel.shared.Selectable

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