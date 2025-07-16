package com.example.repository.mapper.shared

import com.example.entity.category.MovieCategoryType
import com.example.entity.category.TvShowCategoryType

fun Long.mapToMovieCategory(): MovieCategoryType {
    return when (this) {
        28L -> MovieCategoryType.ACTION
        12L -> MovieCategoryType.ADVENTURE
        16L -> MovieCategoryType.ANIMATION
        35L -> MovieCategoryType.COMEDY
        80L -> MovieCategoryType.CRIME
        99L -> MovieCategoryType.DOCUMENTARY
        18L -> MovieCategoryType.DRAMA
        10751L -> MovieCategoryType.FAMILY
        14L -> MovieCategoryType.FANTASY
        36L -> MovieCategoryType.HISTORY
        27L -> MovieCategoryType.HORROR
        10402L -> MovieCategoryType.MUSIC
        9648L -> MovieCategoryType.MYSTERY
        10749L -> MovieCategoryType.ROMANCE
        878L -> MovieCategoryType.SCIENCE_FICTION
        10770L -> MovieCategoryType.TV_MOVIE
        53L -> MovieCategoryType.THRILLER
        10752L -> MovieCategoryType.WAR
        37L -> MovieCategoryType.WESTERN
        else -> MovieCategoryType.ALL
    }
}

fun Long.mapToTvShowCategory(): TvShowCategoryType {
    return when (this) {
        10759L -> TvShowCategoryType.ACTION_ADVENTURE
        16L -> TvShowCategoryType.ANIMATION
        35L -> TvShowCategoryType.COMEDY
        80L -> TvShowCategoryType.CRIME
        99L -> TvShowCategoryType.DOCUMENTARY
        18L -> TvShowCategoryType.DRAMA
        10751L -> TvShowCategoryType.FAMILY
        10762L -> TvShowCategoryType.KIDS
        9648L -> TvShowCategoryType.MYSTERY
        10763L -> TvShowCategoryType.NEWS
        10764L -> TvShowCategoryType.REALITY
        10765L -> TvShowCategoryType.SCIENCE_FICTION_FANTASY
        10766L -> TvShowCategoryType.SOAP
        10767L -> TvShowCategoryType.TALK
        10768L -> TvShowCategoryType.WAR_POLITICS
        37L -> TvShowCategoryType.WESTERN
        10749L -> TvShowCategoryType.ROMANCE
        else -> TvShowCategoryType.ALL
    }
}