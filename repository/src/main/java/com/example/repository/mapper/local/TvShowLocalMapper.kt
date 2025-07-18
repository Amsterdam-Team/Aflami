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
            poster = tvShow.poster,
            productionYear = tvShow.productionYear,
            rating = tvShow.rating,
            popularity = tvShow.popularity,
            seasonCount = tvShow.seasonCount,
            originCountry = tvShow.originCountry,
        )
    }

    fun mapToTvShow(tvShowWithCategory: TvShowWithCategory): TvShow {
        return TvShow(
            id = tvShowWithCategory.tvShow.tvShowId,
            name = tvShowWithCategory.tvShow.name,
            description = tvShowWithCategory.tvShow.description,
            poster = tvShowWithCategory.tvShow.poster,
            productionYear = tvShowWithCategory.tvShow.productionYear,
            rating = tvShowWithCategory.tvShow.rating,
            categories = categoryLocalMapper.mapToTvShowCategories(tvShowWithCategory.categories),
            popularity = tvShowWithCategory.tvShow.popularity,
            seasonCount = tvShowWithCategory.tvShow.seasonCount,
            originCountry = tvShowWithCategory.tvShow.originCountry,
        )
    }
}