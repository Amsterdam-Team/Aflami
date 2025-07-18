package com.example.viewmodel.shared.uiStates

import com.example.entity.category.TvShowGenre
import com.example.viewmodel.shared.Selectable

data class TvGenreItemUiState(
    val selectableTvShowGenre: Selectable<TvShowGenre> = Selectable(
        type = TvShowGenre.ALL,
        isSelected = false
    )
)