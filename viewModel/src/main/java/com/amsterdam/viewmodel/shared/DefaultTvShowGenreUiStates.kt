package com.amsterdam.viewmodel.shared

import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.viewmodel.shared.uiStates.TvShowGenreItemUiState

val defaultTvShowGenres = TvShowGenre.entries.toTypedArray()
    .mapIndexed { index, category ->
        TvShowGenreItemUiState(
            selectableTvShowGenre =
                Selectable(
                    item = category,
                    isSelected = index == 0,
                )
        )
    }