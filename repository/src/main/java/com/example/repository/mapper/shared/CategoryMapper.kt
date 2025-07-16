package com.example.repository.mapper.shared

import com.example.entity.category.MovieGenre
import com.example.entity.category.TvShowGenre

fun Long.mapToMovieCategory(): MovieGenre {
    return when (this) {
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
}

fun Long.mapToTvShowCategory(): TvShowGenre {
    return when (this) {
        10759L -> TvShowGenre.ACTION_ADVENTURE
        16L -> TvShowGenre.ANIMATION
        35L -> TvShowGenre.COMEDY
        80L -> TvShowGenre.CRIME
        99L -> TvShowGenre.DOCUMENTARY
        18L -> TvShowGenre.DRAMA
        10751L -> TvShowGenre.FAMILY
        10762L -> TvShowGenre.KIDS
        9648L -> TvShowGenre.MYSTERY
        10763L -> TvShowGenre.NEWS
        10764L -> TvShowGenre.REALITY
        10765L -> TvShowGenre.SCIENCE_FICTION_FANTASY
        10766L -> TvShowGenre.SOAP
        10767L -> TvShowGenre.TALK
        10768L -> TvShowGenre.WAR_POLITICS
        37L -> TvShowGenre.WESTERN
        else -> TvShowGenre.ALL
    }
}