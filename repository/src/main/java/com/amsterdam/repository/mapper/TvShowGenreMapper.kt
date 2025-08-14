package com.amsterdam.repository.mapper

import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.repository.dto.local.TvShowCategoryLocalDto


fun TvShowCategoryLocalDto.toEntity(): TvShowGenre = when (categoryId) {
    28L -> TvShowGenre.ACTION_ADVENTURE
    16L -> TvShowGenre.ANIMATION
    35L -> TvShowGenre.COMEDY
    80L -> TvShowGenre.CRIME
    99L -> TvShowGenre.DOCUMENTARY
    18L -> TvShowGenre.DRAMA
    10751L -> TvShowGenre.FAMILY
    9648L -> TvShowGenre.MYSTERY
    37L -> TvShowGenre.WESTERN
    10762L -> TvShowGenre.KIDS
    10763L -> TvShowGenre.NEWS
    10764L -> TvShowGenre.REALITY
    10765L -> TvShowGenre.SOAP
    10766L -> TvShowGenre.TALK
    10767L -> TvShowGenre.WAR_POLITICS
    10768L -> TvShowGenre.SCIENCE_FICTION_FANTASY
    else -> TvShowGenre.ALL

}

fun TvShowGenre.toDto(): Long = when (this) {
    TvShowGenre.ACTION_ADVENTURE -> 28L
    TvShowGenre.ANIMATION -> 16L
    TvShowGenre.COMEDY -> 35L
    TvShowGenre.CRIME -> 80L
    TvShowGenre.DOCUMENTARY -> 99L
    TvShowGenre.DRAMA -> 18L
    TvShowGenre.FAMILY -> 10751L
    TvShowGenre.MYSTERY -> 9648L
    TvShowGenre.WESTERN -> 37L
    TvShowGenre.KIDS -> 10762L
    TvShowGenre.NEWS -> 10763L
    TvShowGenre.REALITY -> 10764L
    TvShowGenre.SOAP -> 10765L
    TvShowGenre.TALK -> 10766L
    TvShowGenre.WAR_POLITICS -> 10767L
    TvShowGenre.SCIENCE_FICTION_FANTASY -> 10768L
    TvShowGenre.ALL -> 35L


}

fun List<TvShowGenre>.toDtoList(): List<Long> = map(TvShowGenre::toDto)

fun toTvShowGenre(id: Long): TvShowGenre {
    return when (id) {
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