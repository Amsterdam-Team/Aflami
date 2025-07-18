package com.example.viewmodel.shared.uiStates

import com.example.entity.category.MovieGenre
import com.example.viewmodel.shared.Selectable

data class MovieGenreItemUiState(
    val selectableMovieGenre: Selectable<MovieGenre> = Selectable(
        type = MovieGenre.ALL,
        isSelected = false
    )
)

