package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.MovieWatchHistoryDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto
import kotlinx.coroutines.flow.Flow

interface WatchHistoryLocalDataSource {
    suspend fun upsertMovieToWatchHistory(movieWatchHistoryDto: MovieWatchHistoryDto)

    fun getMoviesWatchHistory(page: Int, pageSize: Int): Flow<List<MovieWatchHistoryDto>>

    suspend fun upsertTvShowToWatchHistory(tvShowWatchHistoryDto: TvShowWatchHistoryDto)

    fun getTvShowsWatchHistory(page: Int, pageSize: Int): Flow<List<TvShowWatchHistoryDto>>
}