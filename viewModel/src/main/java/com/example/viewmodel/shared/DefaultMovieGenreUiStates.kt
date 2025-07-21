package com.example.viewmodel.shared

import com.example.entity.category.MovieGenre
import com.example.viewmodel.shared.uiStates.MovieGenreItemUiState

val defaultMovieGenres =
    MovieGenre.entries.toTypedArray().mapIndexed { index, category ->
        MovieGenreItemUiState(
            selectableMovieGenre =
                Selectable(
                    item = category,
                    isSelected = index == 0,
                ),
        )
    }