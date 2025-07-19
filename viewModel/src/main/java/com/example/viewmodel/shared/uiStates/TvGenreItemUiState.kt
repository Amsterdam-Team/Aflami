package com.example.viewmodel.shared.uiStates

import com.example.entity.category.TvShowGenre
import com.example.viewmodel.shared.Selectable

data class TvGenreItemUiState(
    val selectableTvShowGenre: Selectable<TvShowGenre> = Selectable(
        item = TvShowGenre.ALL,
        isSelected = false
    )
)