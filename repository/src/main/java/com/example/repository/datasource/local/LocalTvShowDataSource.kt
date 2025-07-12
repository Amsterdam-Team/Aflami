package com.example.repository.datasource.local

import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.relation.TvShowWithCategories
import com.example.repository.dto.local.utils.SearchType

interface LocalTvShowDataSource {
    suspend fun getTvShowsByKeywordAndSearchType(
        keyword: String,
        searchType: SearchType
    ): List<TvShowWithCategories>

    suspend fun addTvShowsWithSearchData(
        movies: List<LocalTvShowDto>,
        searchKeyword: String,
        searchType: SearchType,
    )
}