package com.example.repository.datasource.local

import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.relation.TvShowWithCategory

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
        categories: List<LocalTvShowCategoryDto>,
        storedLanguage: String
    )

    suspend fun incrementGenreInterest(categoryId: Long)
}