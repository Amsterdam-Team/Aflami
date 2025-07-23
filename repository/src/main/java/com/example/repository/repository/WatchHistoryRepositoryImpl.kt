package com.example.repository.repository

import com.example.domain.repository.WatchHistoryRepository
import com.example.entity.Movie
import com.example.entity.WatchHistory
import com.example.repository.datasource.local.WatchHistoryLocalDataSource
import com.example.repository.mapper.local.MovieLocalMapper
import com.example.repository.mapper.local.WatchHistoryMapper

class WatchHistoryRepositoryImpl(
    private val local: WatchHistoryLocalDataSource,
    private val movieLocalMapper: MovieLocalMapper,
    private val watchHistoryMapper: WatchHistoryMapper
) : WatchHistoryRepository {

    override suspend fun addToWatchHistory(item: WatchHistory) {
        local.addToWatchHistory(watchHistoryMapper.toDto(item, emptyList()))
    }

    override suspend fun getContinueWatchingMovies(): List<Movie> {
        return movieLocalMapper.toEntityList(local.getContinueWatching())
    }
}