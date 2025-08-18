package com.amsterdam.domain.repository

import com.amsterdam.domain.utils.MovieWatchHistory
import com.amsterdam.domain.utils.TvShowWatchHistory
import kotlinx.coroutines.flow.Flow

interface WatchHistoryRepository {
    suspend fun addMovieToWatchHistory(movieId: Long)
    fun getContinueWatchingMovies(page: Int, pageSize: Int): Flow<List<MovieWatchHistory>>

    suspend fun addTvShowToWatchHistory(tvShowId: Long)
    fun getContinueWatchingTvShows(page: Int, pageSize: Int): Flow<List<TvShowWatchHistory>>
}