package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto
import kotlinx.coroutines.flow.Flow

interface WatchHistoryLocalDataSource {
    suspend fun addMovieToWatchHistory(item: MovieWatchHistoryDto)
    fun getMovieContinueWatching(): Flow<List<LocalMovieDto>>
    suspend fun addTvShowToWatchHistory(item: TvShowWatchHistoryDto)
    fun getTvShowContinueWatching(): Flow<List<LocalTvShowDto>>
}