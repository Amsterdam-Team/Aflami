package com.amsterdam.domain.repository

import com.amsterdam.entity.Movie
import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.entity.TvShow
import com.amsterdam.entity.TvShowWatchHistory
import kotlinx.coroutines.flow.Flow

interface WatchHistoryRepository {
    suspend fun addMovieToWatchHistory(item: MovieWatchHistory)
    fun getContinueWatchingMovies(): Flow<List<Movie>>
    suspend fun addTvShowToWatchHistory(item: TvShowWatchHistory)
    fun getContinueWatchingTvShows(): Flow<List<TvShow>>
}