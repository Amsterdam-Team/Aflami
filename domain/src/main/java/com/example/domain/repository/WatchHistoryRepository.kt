package com.example.domain.repository

import com.example.entity.Movie
import com.example.entity.WatchHistory

interface WatchHistoryRepository {
    suspend fun addToWatchHistory(item: WatchHistory)
    suspend fun getContinueWatchingMovies(): List<Movie>
}