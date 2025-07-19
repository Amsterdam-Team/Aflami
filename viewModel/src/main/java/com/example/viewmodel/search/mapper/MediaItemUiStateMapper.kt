package com.example.viewmodel.search.mapper

import android.icu.text.DecimalFormat
import com.example.entity.TvShow
import com.example.viewmodel.shared.uiStates.TvShowItemUiState

private fun TvShow.toMediaItemUiState(): TvShowItemUiState =
    TvShowItemUiState(
        id = id,
        name = name,
        posterImageUrl = posterUrl,
        yearOfRelease = productionYear.toString(),
        rate = DecimalFormat("#.#").format(rating).toString()
    )

fun List<TvShow>.toTvShowUiStates() = map(TvShow::toMediaItemUiState)