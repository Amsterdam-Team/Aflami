package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategory
import kotlinx.datetime.Instant

interface TvShowLocalSource {
    suspend fun addTvShowWithCategories(
        tvShow: LocalTvShowDto,
        categoryIds: List<Long>,
        storedLanguage: String
    )

    suspend fun incrementGenreInterest(categoryId: Long)

    suspend fun insertTvShow(tvShow : LocalTvShowDto)

    suspend fun getTvShowById(tvShowId: Long, storedLanguage: String): LocalTvShowDto?

    suspend fun getPopularTvShows(storedLanguage: String): List<TvShowWithCategory>

    suspend fun getTopRatedTvShows(storedLanguage: String): List<LocalTvShowDto>

    suspend fun addPopularTvShows(tvShows: List<LocalTvShowDto>)

    suspend fun deleteExpiredPopularTvShows(expirationTime: Instant, storedLanguage: String)

    suspend fun addTopRatedTvShows(tvShows: List<LocalTvShowDto>)

    suspend fun deleteExpiredTopRatedTvShows(expirationTime: Instant, storedLanguage: String)
}