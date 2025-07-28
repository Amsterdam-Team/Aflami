package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.TvShow
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.mapper.shared.EntityMapper
import com.amsterdam.repository.mapper.shared.mapCategoryIdToTvShowGenre
import com.amsterdam.repository.utils.toSafeLocalDate
import javax.inject.Inject

class TvShowDetailsRemoteMapper @Inject constructor() : EntityMapper<TvShowDetailsRemoteResponse, TvShow> {
    override fun toEntity(dto: TvShowDetailsRemoteResponse): TvShow {
        return TvShow(
            id = dto.id,
            name = dto.title,
            description = dto.overview,
            posterUrl = dto.fullPosterPath.orEmpty(),
            airDate = dto.releaseDate.toSafeLocalDate(),
            categories = dto.genres.map { mapCategoryIdToTvShowGenre(it.id.toLong()) },
            rating = dto.voteAverage.toFloat(),
            popularity = dto.popularity,
            seasonCount = dto.seasonCount,
            originCountry = dto.originCountry.firstOrNull() ?: ""
        )
    }
}