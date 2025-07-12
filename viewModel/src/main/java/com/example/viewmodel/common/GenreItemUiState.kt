package com.example.viewmodel.common

import androidx.annotation.DrawableRes
import com.example.entity.Category

data class GenreItemUiState(
    val id: String = "",
    val genreName: String = "",
    val tabOption: TabOption = TabOption.MOVIES
)

enum class MovieGenreType {
    ALL,
    ROMANCE,
    SCIENCE_FICTION,
    FAMILY,
    MYSTERY,
    HISTORY,
    WAR,
    ACTION,
    CRIME,
    COMEDY,
    HORROR,
    WESTERN,
    MUSIC,
    ADVENTURE,
    TV_MOVIE,
    FANTASY,
    THRILLER,
    DRAMA,
    DOCUMENTARY,
    ANIMATION;

    /*companion object {
        fun toGenreItemsUiState(): List<GenreItemUiState> {
            return MovieGenreType.entries.toTypedArray().mapIndexed { index, genreType ->
                GenreItemUiState(
                    id = TODO(),
                    selectedGenreName = TODO()
                )
            }
        }
    }*/
}

fun Category.toGenreUiStates(tabOption: TabOption): GenreItemUiState {
    return GenreItemUiState(
        id = id.toString(),
        genreName = name,
        tabOption = tabOption
    )
}