package com.example.repository.repository

import com.example.domain.repository.TvShowRepository
import com.example.entity.TvShow

class TvShowRepositoryImpl() : TvShowRepository {
    override suspend fun getTvShowByKeyword(keyword: String): List<TvShow> {
        TODO("Not yet implemented")
    }
}