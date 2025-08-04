package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.TvShow
import com.amsterdam.repository.dto.local.LocalTvShowDto

fun LocalTvShowDto.toTvShowEntity(): TvShow =
    TvShow(
        id = tvShowId,
        name = name,
        description = description,
        posterUrl = poster,
        airDate = airDate,
        rating = rating,
        categories = emptyList(),
        popularity = popularity,
        seasonCount = seasonCount,
        originCountry = originCountry,
        productionCompanies = emptyList()
    )


fun TvShow.LocalTvShowDto(storedLanguage: String): LocalTvShowDto =
    LocalTvShowDto(
        tvShowId = id,
        storedLanguage = storedLanguage,
        name = name,
        description = description,
        poster = posterUrl,
        airDate = airDate,
        rating = rating,
        popularity = popularity,
        seasonCount = seasonCount,
        originCountry = originCountry
    )
