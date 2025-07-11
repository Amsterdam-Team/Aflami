package com.example.domain.repository

import com.example.entity.Movie
import com.example.entity.TvShow

interface TvShowRepository {
    suspend fun getTvShowByKeyword(keyword: String): List<TvShow>
}