package com.example.repository.datasource.local

import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.WatchHistoryDto
import kotlinx.coroutines.flow.Flow

interface WatchHistoryLocalDataSource {
    suspend fun addToWatchHistory(item: WatchHistoryDto)
    suspend fun getContinueWatching(): Flow<List<LocalMovieDto>>
}