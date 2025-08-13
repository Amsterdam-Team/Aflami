package com.amsterdam.viewmodel.shared.uiStates

import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.viewmodel.shared.Selectable

data class TvShowGenreItemUiState(
    val selectableTvShowGenre: Selectable<TvShowGenre> = Selectable(
        item = TvShowGenre.ALL,
        isSelected = false
    )
)

