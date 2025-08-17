package com.amsterdam.viewmodel.shared

import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.shared.uiStates.MovieGenreItemUiState

val defaultMovieGenres = MovieGenre.entries.toTypedArray()
    .mapIndexed { index, category ->
        MovieGenreItemUiState(
            selectableMovieGenre =
                Selectable(
                    item = category,
                    isSelected = index == 0,
                ),
        )
    }