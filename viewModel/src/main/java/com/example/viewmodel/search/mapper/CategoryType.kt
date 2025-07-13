package com.example.viewmodel.search.mapper

import com.example.entity.GenreType

enum class CategoryType {
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

fun GenreType.toCategoryType(): CategoryType{
    return when(this){
        GenreType.ALL -> CategoryType.ALL
        GenreType.ROMANCE -> CategoryType.ROMANCE
        GenreType.SCIENCE_FICTION -> CategoryType.SCIENCE_FICTION
        GenreType.FAMILY -> CategoryType.FAMILY
        GenreType.MYSTERY -> CategoryType.MYSTERY
        GenreType.HISTORY -> CategoryType.HISTORY
        GenreType.WAR -> CategoryType.WAR
        GenreType.ACTION -> CategoryType.ACTION
        GenreType.CRIME -> CategoryType.CRIME
        GenreType.COMEDY -> CategoryType.COMEDY
        GenreType.HORROR -> CategoryType.HORROR
        GenreType.WESTERN -> CategoryType.WESTERN
        GenreType.MUSIC -> CategoryType.MUSIC
        GenreType.ADVENTURE -> CategoryType.ADVENTURE
        GenreType.TV_MOVIE -> CategoryType.TV_MOVIE
        GenreType.FANTASY -> CategoryType.FANTASY
        GenreType.THRILLER -> CategoryType.THRILLER
        GenreType.DRAMA -> CategoryType.DRAMA
        GenreType.DOCUMENTARY -> CategoryType.DOCUMENTARY
        GenreType.ANIMATION -> CategoryType.ANIMATION
    }
}

fun CategoryType.toGenreType(): GenreType {
    return when (this) {
        CategoryType.ALL -> GenreType.ALL
        CategoryType.ROMANCE -> GenreType.ROMANCE
        CategoryType.SCIENCE_FICTION -> GenreType.SCIENCE_FICTION
        CategoryType.FAMILY -> GenreType.FAMILY
        CategoryType.MYSTERY -> GenreType.MYSTERY
        CategoryType.HISTORY -> GenreType.HISTORY
        CategoryType.WAR -> GenreType.WAR
        CategoryType.ACTION -> GenreType.ACTION
        CategoryType.CRIME -> GenreType.CRIME
        CategoryType.COMEDY -> GenreType.COMEDY
        CategoryType.HORROR -> GenreType.HORROR
        CategoryType.WESTERN -> GenreType.WESTERN
        CategoryType.MUSIC -> GenreType.MUSIC
        CategoryType.ADVENTURE -> GenreType.ADVENTURE
        CategoryType.TV_MOVIE -> GenreType.TV_MOVIE
        CategoryType.FANTASY -> GenreType.FANTASY
        CategoryType.THRILLER -> GenreType.THRILLER
        CategoryType.DRAMA -> GenreType.DRAMA
        CategoryType.DOCUMENTARY -> GenreType.DOCUMENTARY
        CategoryType.ANIMATION -> GenreType.ANIMATION
    }
}