package com.example.repository.datasource.local

import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.WatchHistoryDto

interface WatchHistoryLocalDataSource {
    suspend fun addToWatchHistory(item: WatchHistoryDto)
    suspend fun getContinueWatching(): List<LocalMovieDto>
}