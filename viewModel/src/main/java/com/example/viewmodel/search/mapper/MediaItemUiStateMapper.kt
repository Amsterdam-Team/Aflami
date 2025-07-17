package com.example.viewmodel.search.mapper

import android.icu.text.DecimalFormat
import com.example.entity.Movie
import com.example.entity.TvShow
import com.example.viewmodel.shared.MediaItemUiState
import com.example.viewmodel.shared.MediaType

private fun Movie.toMediaItemUiState(): MediaItemUiState =
    MediaItemUiState(
        id = id,
        name = name,
        posterImageUrl = poster,
        mediaType = MediaType.MOVIE,
        yearOfRelease = productionYear.toString(),
        rate = DecimalFormat("#.#").format(rating).toString()
    )

fun List<Movie>.toMoveUiStates() = map(Movie::toMediaItemUiState)

private fun TvShow.toMediaItemUiState(): MediaItemUiState =
    MediaItemUiState(
        id = id,
        name = name,
        posterImageUrl = poster,
        mediaType = MediaType.TV_SHOW,
        yearOfRelease = productionYear.toString(),
        rate = DecimalFormat("#.#").format(rating).toString()
    )

fun List<TvShow>.toTvShowUiStates() = map(TvShow::toMediaItemUiState)