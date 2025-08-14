package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategories
import kotlinx.datetime.Instant

interface TvShowLocalDataSource {
    suspend fun upsertTvShowWithCategories(
        tvShow: TvShowLocalDto,
        categoryIds: List<Long>,
        storedLanguage: String
    )

    suspend fun incrementGenreInterest(categoryId: Long)

    suspend fun upsertTvShow(tvShow: TvShowLocalDto)

    suspend fun getTvShowById(tvShowId: Long, storedLanguage: String): TvShowLocalDto?

    suspend fun getPopularTvShows(storedLanguage: String): List<TvShowWithCategories>

    suspend fun getTopRatedTvShows(storedLanguage: String): List<TvShowLocalDto>

    suspend fun upsertPopularTvShows(tvShows: List<TvShowLocalDto>)

    suspend fun deleteExpiredPopularTvShows(expirationTime: Instant, storedLanguage: String)

    suspend fun upsertTopRatedTvShows(tvShows: List<TvShowLocalDto>)

    suspend fun deleteExpiredTopRatedTvShows(expirationTime: Instant, storedLanguage: String)
}