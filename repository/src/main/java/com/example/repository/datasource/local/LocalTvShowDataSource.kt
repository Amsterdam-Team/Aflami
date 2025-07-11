package com.example.repository.datasource.local

import com.example.repository.dto.local.LocalTvShowDto

interface LocalTvShowDataSource {
    suspend fun getTvShowsBySearchKeyword(searchKeyword: String): List<LocalTvShowDto>
    suspend fun addAllTvShows(
        tvShows: List<LocalTvShowDto>,
        searchKeyword: String
    )
}