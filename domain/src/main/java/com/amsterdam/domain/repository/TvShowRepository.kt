package com.amsterdam.domain.repository

import com.amsterdam.entity.TvShow

interface TvShowRepository {
    suspend fun getTvShowByKeyword(keyword: String): List<TvShow>
}