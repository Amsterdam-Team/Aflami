package com.example.viewmodel.common

data class GenreItemUiState(
    val type: GenreType = GenreType.ALL,
    val isSelected: Boolean = true
)

enum class GenreType {
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

    companion object {
        fun toGenreItemsUiState(): List<GenreItemUiState> {
            return GenreType.entries.toTypedArray().mapIndexed { index, genreType ->
                GenreItemUiState(
                    type = genreType,
                    isSelected = index == 0
                )
            }
        }
    }
}