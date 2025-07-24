package com.example.repository.repository

import com.example.domain.repository.WatchHistoryRepository
import com.example.entity.Movie
import com.example.entity.WatchHistory
import com.example.repository.datasource.local.WatchHistoryLocalDataSource
import com.example.repository.mapper.local.MovieLocalMapper
import com.example.repository.mapper.local.WatchHistoryMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WatchHistoryRepositoryImpl(
    private val watchHistoryLocalDataSource: WatchHistoryLocalDataSource,
    private val movieLocalMapper: MovieLocalMapper,
    private val watchHistoryMapper: WatchHistoryMapper
) : WatchHistoryRepository {

    override suspend fun addToWatchHistory(item: WatchHistory) {
        watchHistoryLocalDataSource.addToWatchHistory(watchHistoryMapper.toDto(item, emptyList()))
    }

    override suspend fun getContinueWatchingMovies(): Flow<List<Movie>> {
        return watchHistoryLocalDataSource.getContinueWatching().map {
            movieLocalMapper.toEntityList(it)
        }
    }

}