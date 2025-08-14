package com.amsterdam.repository.mapper

import com.amsterdam.entity.TvShow
import com.amsterdam.repository.dto.local.relation.TvShowWithCategory

fun TvShowWithCategory.toEntity(): TvShow =
    TvShow(
        id = tvShow.tvShowId,
        name = tvShow.name,
        description = tvShow.description,
        posterUrl = tvShow.poster,
        airDate = tvShow.airDate,
        rating = tvShow.rating,
        categories = categories
            .distinctBy { it.categoryId }
            .map { toTvShowGenre(it.categoryId) },
        popularity = tvShow.popularity,
        seasonCount = tvShow.seasonCount,
        originCountry = tvShow.originCountry
    )