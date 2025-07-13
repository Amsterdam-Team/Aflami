package com.example.viewmodel.common.categories

import com.example.domain.useCase.genreTypes.TvShowGenre

enum class TvShowCategoryType {
    ALL,
    ACTION_ADVENTURE,
    ANIMATION,
    COMEDY,
    CRIME,
    DOCUMENTARY,
    DRAMA,
    FAMILY,
    KIDS,
    MYSTERY,
    NEWS,
    REALITY,
    SCIENCE_FICTION,
    FANTASY,
    SOAP,
    TALK,
    WAR_POLITICS,
    WESTERN,
    ROMANCE,
}

fun TvShowGenre.toTvShowCategoryType(): TvShowCategoryType {
    return when(this){
        TvShowGenre.ALL -> TvShowCategoryType.ALL
        TvShowGenre.ACTION_ADVENTURE -> TvShowCategoryType.ACTION_ADVENTURE
        TvShowGenre.ANIMATION -> TvShowCategoryType.ANIMATION
        TvShowGenre.COMEDY -> TvShowCategoryType.COMEDY
        TvShowGenre.CRIME -> TvShowCategoryType.CRIME
        TvShowGenre.DOCUMENTARY -> TvShowCategoryType.DOCUMENTARY
        TvShowGenre.DRAMA -> TvShowCategoryType.DRAMA
        TvShowGenre.FAMILY -> TvShowCategoryType.FAMILY
        TvShowGenre.KIDS -> TvShowCategoryType.KIDS
        TvShowGenre.MYSTERY -> TvShowCategoryType.MYSTERY
        TvShowGenre.NEWS -> TvShowCategoryType.NEWS
        TvShowGenre.REALITY -> TvShowCategoryType.REALITY
        TvShowGenre.SCIENCE_FICTION -> TvShowCategoryType.SCIENCE_FICTION
        TvShowGenre.FANTASY -> TvShowCategoryType.FANTASY
        TvShowGenre.SOAP -> TvShowCategoryType.SOAP
        TvShowGenre.TALK -> TvShowCategoryType.TALK
        TvShowGenre.WAR_POLITICS -> TvShowCategoryType.WAR_POLITICS
        TvShowGenre.WESTERN -> TvShowCategoryType.WESTERN
        TvShowGenre.ROMANCE -> TvShowCategoryType.ROMANCE
    }
}

fun TvShowCategoryType.toTvShowGenre(): TvShowGenre {
    return when (this) {
        TvShowCategoryType.ALL -> TvShowGenre.ALL
        TvShowCategoryType.ACTION_ADVENTURE -> TvShowGenre.ACTION_ADVENTURE
        TvShowCategoryType.ANIMATION -> TvShowGenre.ANIMATION
        TvShowCategoryType.COMEDY -> TvShowGenre.COMEDY
        TvShowCategoryType.CRIME -> TvShowGenre.CRIME
        TvShowCategoryType.DOCUMENTARY -> TvShowGenre.DOCUMENTARY
        TvShowCategoryType.DRAMA -> TvShowGenre.DRAMA
        TvShowCategoryType.FAMILY -> TvShowGenre.FAMILY
        TvShowCategoryType.KIDS -> TvShowGenre.KIDS
        TvShowCategoryType.MYSTERY -> TvShowGenre.MYSTERY
        TvShowCategoryType.NEWS -> TvShowGenre.NEWS
        TvShowCategoryType.REALITY -> TvShowGenre.REALITY
        TvShowCategoryType.SCIENCE_FICTION -> TvShowGenre.SCIENCE_FICTION
        TvShowCategoryType.FANTASY -> TvShowGenre.FANTASY
        TvShowCategoryType.SOAP -> TvShowGenre.SOAP
        TvShowCategoryType.TALK -> TvShowGenre.TALK
        TvShowCategoryType.WAR_POLITICS -> TvShowGenre.WAR_POLITICS
        TvShowCategoryType.WESTERN -> TvShowGenre.WESTERN
        TvShowCategoryType.ROMANCE -> TvShowGenre.ROMANCE
    }
}
