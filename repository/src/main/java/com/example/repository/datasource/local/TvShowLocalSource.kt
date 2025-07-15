package com.example.repository.datasource.local

import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.relation.TvShowWithCategory

interface TvShowLocalSource {
    suspend fun getTvShowsBy(searchKeyword: String): List<TvShowWithCategory>
    suspend fun addTvShows(tvShows: List<LocalTvShowDto>, searchKeyword: String)
}