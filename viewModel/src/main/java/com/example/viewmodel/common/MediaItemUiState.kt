package com.example.viewmodel.common

import android.icu.text.DecimalFormat
import com.example.entity.Movie
import com.example.entity.TvShow

const val base_image_url = "https://image.tmdb.org/t/p/w500"

data class MediaItemUiState(
    val name: String = "",
    val posterImage: String = "",
    val mediaType: MediaType = MediaType.MOVIE,
    val yearOfRelease: String = "",
    val rate: String = ""
)

enum class MediaType {
    MOVIE,
    TV_SHOW,
}

fun Movie.toMediaItemUiState(): MediaItemUiState =
    MediaItemUiState(
        name = name,
        posterImage = base_image_url + poster,
        mediaType = MediaType.MOVIE,
        yearOfRelease = productionYear.toString(),
        rate = DecimalFormat("#.#").format(rating).toString()
    )

fun List<Movie>.toMoveUiStates() = map(Movie::toMediaItemUiState)

fun TvShow.toMediaItemUiState(): MediaItemUiState =
    MediaItemUiState(
        name = name,
        posterImage = base_image_url + poster,
        mediaType = MediaType.TV_SHOW,
        yearOfRelease = productionYear.toString(),
        rate = DecimalFormat("#.#").format(rating).toString()
    )

fun List<TvShow>.toTvShowUiStates() = map(TvShow::toMediaItemUiState)
