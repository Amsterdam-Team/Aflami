package com.example.domain.repository

import com.example.domain.useCase.genreTypes.TvShowGenre
import com.example.entity.TvShow

interface TvShowRepository {
    suspend fun getTvShowByKeyword(keyword: String, rating: Float = 0f, tvShowGenre: TvShowGenre = TvShowGenre.ALL): List<TvShow>
}