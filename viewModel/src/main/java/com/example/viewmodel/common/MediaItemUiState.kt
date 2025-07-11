package com.example.viewmodel.common

import com.example.entity.Movie
import com.example.entity.TvShow

data class MediaItemUiState(
    val name: String = "",
    val posterImage: String = "",
    val mediaType: MediaType = MediaType.MOVIE,
    val yearOfRelease: String = "",
    val rate: String = ""

)

enum class MediaType() {
    MOVIE,
    TV_SHOW,
}

fun Movie.toMediaItemUiState(): MediaItemUiState =
    MediaItemUiState(
        name = name,
        posterImage = poster,
        mediaType = MediaType.MOVIE,
        yearOfRelease = productionYear.toString(),
        rate = rating.toString()
    )

fun TvShow.toMediaItemUiState(): MediaItemUiState =
    MediaItemUiState(
        name = name,
        posterImage = poster,
        mediaType = MediaType.TV_SHOW,
        yearOfRelease = productionYear.toString(),
        rate = rating.toString()
    )