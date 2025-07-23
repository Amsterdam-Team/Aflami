package com.example.repository.datasource.local

import com.example.entity.category.TvShowGenre
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.relation.TvShowWithCategory
import com.example.repository.dto.local.utils.SearchType

interface TvShowLocalSource {
    suspend fun getTvShowsByKeywordAndSearchType(
        searchKeyword: String,
        searchType: SearchType,
        storedLanguage: String,
        limit: Int,
        offset: Int
    ): List<TvShowWithCategory>

    suspend fun addTvShows(
        tvShows: List<LocalTvShowDto>,
        searchKeyword: String,
        storedLanguage: String
    )

    suspend fun incrementGenreInterest(genre: TvShowGenre)

    suspend fun getAllGenreInterests(): Map<TvShowGenre, Int>
}