package com.example.viewmodel.common

import com.example.entity.Movie
import com.example.entity.TvShow

val base_image_url = "https://image.tmdb.org/t/p/w500"

data class MediaItemUiState(
    val name: String = "",
    val posterImage: String = base_image_url,
    val mediaType: MediaType = MediaType.MOVIE,
    val yearOfRelease: String = "",
    val rate: String = ""

)

enum class MediaType {
    MOVIE,
    TV_SHOW,
}

private fun Movie.toMediaItemUiState(): MediaItemUiState =
    MediaItemUiState(
        name = name,
        posterImage = base_image_url + poster,
        mediaType = MediaType.MOVIE,
        yearOfRelease = productionYear.toString(),
        rate = rating.toString()
    )

fun List<Movie>.toMoveUiStates() = map(Movie::toMediaItemUiState)

private fun TvShow.toMediaItemUiState(): MediaItemUiState =
    MediaItemUiState(
        name = name,
        posterImage = base_image_url + poster,
        mediaType = MediaType.TV_SHOW,
        yearOfRelease = productionYear.toString(),
        rate = rating.toString()
    )

fun List<TvShow>.toTvShowUiStates() = map(TvShow::toMediaItemUiState)
