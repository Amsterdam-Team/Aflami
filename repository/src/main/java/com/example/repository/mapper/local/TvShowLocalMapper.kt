package com.example.repository.mapper.local

import com.example.entity.TvShow
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.relation.TvShowWithCategory

class TvShowLocalMapper(
    private val categoryLocalMapper: CategoryLocalMapper
) {

    fun mapToTvShows(tvShowWithCategories: List<TvShowWithCategory>): List<TvShow> {
        return tvShowWithCategories.map { mapToTvShow(it) }
    }

    fun mapToLocalTvShows(tvShows: List<TvShow>): List<LocalTvShowDto> {
        return tvShows.map { mapToLocalTvShow(it) }
    }

    fun mapToLocalTvShow(tvShow: TvShow): LocalTvShowDto {
        return LocalTvShowDto(
            tvShowId = tvShow.id,
            name = tvShow.name,
            description = tvShow.description,
            poster = tvShow.posterUrl,
            productionYear = tvShow.productionYear.toInt(),
            rating = tvShow.rating,
            popularity = tvShow.popularity
        )
    }

    fun mapToTvShow(tvShowWithCategory: TvShowWithCategory): TvShow {
        return TvShow(
            id = tvShowWithCategory.tvShow.tvShowId,
            name = tvShowWithCategory.tvShow.name,
            description = tvShowWithCategory.tvShow.description,
            posterUrl = tvShowWithCategory.tvShow.poster,
            productionYear = tvShowWithCategory.tvShow.productionYear.toUInt(),
            rating = tvShowWithCategory.tvShow.rating,
            categories = categoryLocalMapper.mapToTvShowCategories(tvShowWithCategory.categories),
            popularity = tvShowWithCategory.tvShow.popularity
        )
    }
}