package com.example.repository.mapper

import com.example.entity.TvShow
import com.example.entity.category.TvShowGenre
import com.example.repository.dto.local.relation.TvShowWithCategory

fun TvShowWithCategory.toDomain(): TvShow {
    return TvShow(
        id = tvShow.tvShowId,
        name = tvShow.name,
        description = tvShow.description,
        poster = tvShow.poster,
        productionYear = tvShow.productionYear,
        rating = tvShow.rating,
        popularity = tvShow.popularity,
        categories = categories.mapNotNull { category ->
            try {
                TvShowGenre.valueOf(category.name.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    )
}
