package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto

fun LocalMovieCategoryDto.toMovieGenreEntity(): MovieGenre = when (categoryId) {
    28L -> MovieGenre.ACTION
    12L -> MovieGenre.ADVENTURE
    16L -> MovieGenre.ANIMATION
    35L -> MovieGenre.COMEDY
    80L -> MovieGenre.CRIME
    99L -> MovieGenre.DOCUMENTARY
    18L -> MovieGenre.DRAMA
    10751L -> MovieGenre.FAMILY
    14L -> MovieGenre.FANTASY
    36L -> MovieGenre.HISTORY
    27L -> MovieGenre.HORROR
    10402L -> MovieGenre.MUSIC
    9648L -> MovieGenre.MYSTERY
    10749L -> MovieGenre.ROMANCE
    878L -> MovieGenre.SCIENCE_FICTION
    10770L -> MovieGenre.TV_MOVIE
    53L -> MovieGenre.THRILLER
    10752L -> MovieGenre.WAR
    37L -> MovieGenre.WESTERN
    else -> MovieGenre.ALL
}

fun MovieGenre.toDto(): Long = when (this) {
    MovieGenre.ACTION -> 28L
    MovieGenre.ADVENTURE -> 12L
    MovieGenre.ANIMATION -> 16L
    MovieGenre.COMEDY -> 35L
    MovieGenre.CRIME -> 80L
    MovieGenre.DOCUMENTARY -> 99L
    MovieGenre.DRAMA -> 18L
    MovieGenre.FAMILY -> 10751L
    MovieGenre.FANTASY -> 14L
    MovieGenre.HISTORY -> 36L
    MovieGenre.HORROR -> 27L
    MovieGenre.MUSIC -> 10402L
    MovieGenre.MYSTERY -> 9648L
    MovieGenre.ROMANCE -> 10749L
    MovieGenre.SCIENCE_FICTION -> 878L
    MovieGenre.TV_MOVIE -> 10770L
    MovieGenre.THRILLER -> 53L
    MovieGenre.WAR -> 10752L
    MovieGenre.WESTERN -> 37L
    MovieGenre.ALL -> 35L
}

fun List<MovieGenre>.toDtoList(): List<Long> = map(MovieGenre::toDto)
