package com.example.viewmodel.common

import kotlin.collections.map

data class GenreItemUiState(
    val type: GenreType = GenreType.ALL,
    val isSelected: Boolean = true
){
    companion object{
        fun List<GenreItemUiState>.selectByType(type: GenreType): List<GenreItemUiState> {
            return map { it.copy(isSelected = it.type == type) }
        }

        fun List<GenreItemUiState>.getSelectedOne(genres: List<GenreItemUiState>): GenreItemUiState {
            return this.first { it.isSelected == true }
        }
    }
}

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
}