package com.example.domain.repository

import com.example.entity.GenreType
import com.example.entity.TvShow

interface TvShowRepository {
    suspend fun getTvShowByKeyword(keyword: String, rating: Float = 0f, genreType: GenreType = GenreType.ALL): List<TvShow>
}