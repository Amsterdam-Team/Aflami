package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategory

interface LocalTvShowDataSource {
    suspend fun getTvShowsBySearchKeyword(searchKeyword: String): List<TvShowWithCategory>
    suspend fun addAllTvShows(
        tvShows: List<LocalTvShowDto>,
        searchKeyword: String
    )
}