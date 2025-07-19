package com.example.domain.repository

import com.example.entity.TvShow
import com.example.entity.category.TvShowGenre

interface TvShowRepository {
    suspend fun getTvShowByKeyword(keyword: String): List<TvShow>

    suspend fun incrementGenreInterest(genre: TvShowGenre)
    suspend fun getAllGenreInterests(): Map<TvShowGenre, Int>
}