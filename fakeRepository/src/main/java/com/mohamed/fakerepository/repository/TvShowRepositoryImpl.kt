package com.mohamed.fakerepository.repository

import com.example.domain.repository.TvShowRepository
import com.example.entity.TvShow

class TvShowRepositoryImpl() : TvShowRepository {
    override suspend fun getTvShowByKeyword(keyword: String): List<TvShow> {
        return emptyList()
    }
}