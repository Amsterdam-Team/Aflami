package com.example.viewmodel.search.mapper

import android.icu.text.DecimalFormat
import com.example.entity.Movie
import com.example.viewmodel.shared.uiStates.MovieItemUiState

private fun Movie.toMediaItemUiState(): MovieItemUiState =
    MovieItemUiState(
        id = id,
        name = name,
        posterImageUrl = posterUrl,
        yearOfRelease = productionYear.toString(),
        rate = DecimalFormat("#.#").format(rating).toString()
    )

fun List<Movie>.toMoveUiStates() = map(Movie::toMediaItemUiState)
