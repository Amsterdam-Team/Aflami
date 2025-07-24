package com.example.domain.repository

import com.example.entity.Movie
import com.example.entity.WatchHistory
import kotlinx.coroutines.flow.Flow

interface WatchHistoryRepository {
    suspend fun addToWatchHistory(item: WatchHistory)
    suspend fun getContinueWatchingMovies(): Flow<List<Movie>>
}