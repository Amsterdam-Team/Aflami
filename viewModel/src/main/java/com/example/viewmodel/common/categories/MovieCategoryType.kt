package com.example.viewmodel.common.categories

import com.example.domain.useCase.genreTypes.MovieGenre

enum class MovieCategoryType {
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

fun MovieGenre.toMovieCategoryType(): MovieCategoryType{
    return when(this){
        MovieGenre.ALL -> MovieCategoryType.ALL
        MovieGenre.ROMANCE -> MovieCategoryType.ROMANCE
        MovieGenre.SCIENCE_FICTION -> MovieCategoryType.SCIENCE_FICTION
        MovieGenre.FAMILY -> MovieCategoryType.FAMILY
        MovieGenre.MYSTERY -> MovieCategoryType.MYSTERY
        MovieGenre.HISTORY -> MovieCategoryType.HISTORY
        MovieGenre.WAR -> MovieCategoryType.WAR
        MovieGenre.ACTION -> MovieCategoryType.ACTION
        MovieGenre.CRIME -> MovieCategoryType.COMEDY
        MovieGenre.COMEDY -> MovieCategoryType.COMEDY
        MovieGenre.HORROR -> MovieCategoryType.HORROR
        MovieGenre.WESTERN -> MovieCategoryType.WESTERN
        MovieGenre.MUSIC -> MovieCategoryType.MUSIC
        MovieGenre.ADVENTURE -> MovieCategoryType.ADVENTURE
        MovieGenre.TV_MOVIE -> MovieCategoryType.TV_MOVIE
        MovieGenre.FANTASY -> MovieCategoryType.FANTASY
        MovieGenre.THRILLER -> MovieCategoryType.THRILLER
        MovieGenre.DRAMA -> MovieCategoryType.DRAMA
        MovieGenre.DOCUMENTARY -> MovieCategoryType.DOCUMENTARY
        MovieGenre.ANIMATION -> MovieCategoryType.ANIMATION
    }
}

fun MovieCategoryType.toMovieGenreType(): MovieGenre {
    return when (this) {
        MovieCategoryType.ALL -> MovieGenre.ALL
        MovieCategoryType.ROMANCE -> MovieGenre.ROMANCE
        MovieCategoryType.SCIENCE_FICTION -> MovieGenre.SCIENCE_FICTION
        MovieCategoryType.FAMILY -> MovieGenre.FAMILY
        MovieCategoryType.MYSTERY -> MovieGenre.MYSTERY
        MovieCategoryType.HISTORY -> MovieGenre.HISTORY
        MovieCategoryType.WAR -> MovieGenre.WAR
        MovieCategoryType.ACTION -> MovieGenre.ACTION
        MovieCategoryType.CRIME -> MovieGenre.CRIME
        MovieCategoryType.COMEDY -> MovieGenre.COMEDY
        MovieCategoryType.HORROR -> MovieGenre.HORROR
        MovieCategoryType.WESTERN -> MovieGenre.WESTERN
        MovieCategoryType.MUSIC -> MovieGenre.MUSIC
        MovieCategoryType.ADVENTURE -> MovieGenre.ADVENTURE
        MovieCategoryType.TV_MOVIE -> MovieGenre.TV_MOVIE
        MovieCategoryType.FANTASY -> MovieGenre.FANTASY
        MovieCategoryType.THRILLER -> MovieGenre.THRILLER
        MovieCategoryType.DRAMA -> MovieGenre.DRAMA
        MovieCategoryType.DOCUMENTARY -> MovieGenre.DOCUMENTARY
        MovieCategoryType.ANIMATION -> MovieGenre.ANIMATION
    }
}