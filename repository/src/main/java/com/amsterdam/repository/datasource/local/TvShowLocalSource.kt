package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategory

interface TvShowLocalSource {
    suspend fun getTvShowsBySearchKeywordSortedByInterest(
        searchKeyword: String,
        storedLanguage: String,
        limit: Int,
        offset: Int
    ): List<TvShowWithCategory>

    suspend fun addTvShows(
        tvShows: List<LocalTvShowDto>,
        searchKeyword: String,
        storedLanguage: String
    )

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
}