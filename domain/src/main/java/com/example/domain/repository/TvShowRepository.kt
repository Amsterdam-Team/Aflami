package com.example.domain.repository

import com.example.entity.TvShow

interface TvShowRepository {
    suspend fun getTvShowByKeyword(
        keyword: String,
        page: Int
    ): List<TvShow>
}
