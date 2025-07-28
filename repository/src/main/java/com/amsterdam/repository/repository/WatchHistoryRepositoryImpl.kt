package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.entity.Movie
import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.entity.TvShow
import com.amsterdam.entity.TvShowWatchHistory
import com.amsterdam.repository.datasource.local.WatchHistoryLocalDataSource
import com.amsterdam.repository.mapper.local.MovieLocalMapper
import com.amsterdam.repository.mapper.local.MovieWatchHistoryMapper
import com.amsterdam.repository.mapper.local.TvShowLocalMapper
import com.amsterdam.repository.mapper.local.TvShowWatchHistoryMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WatchHistoryRepositoryImpl(
    private val watchHistoryLocalDataSource: WatchHistoryLocalDataSource,
    private val movieLocalMapper: MovieLocalMapper,
    private val tvLocalMapper: TvShowLocalMapper,
    private val movieWatchHistoryMapper: MovieWatchHistoryMapper,
    private val tvShowWatchHistoryMapper: TvShowWatchHistoryMapper
) : WatchHistoryRepository {

    override suspend fun addMovieToWatchHistory(item: MovieWatchHistory) {
        watchHistoryLocalDataSource.addMovieToWatchHistory(
            movieWatchHistoryMapper.toDto(
                item,
                emptyList()
            )
        )
    }

    override fun getContinueWatchingMovies(): Flow<List<Movie>> {
        return watchHistoryLocalDataSource.getMovieContinueWatching().map {
            movieLocalMapper.toEntityList(it)
        }
    }

    override suspend fun addTvShowToWatchHistory(item: TvShowWatchHistory) {
        watchHistoryLocalDataSource.addTvShowToWatchHistory(
            tvShowWatchHistoryMapper.toDto(
                item,
                emptyList()
            )
        )
    }

    override fun getContinueWatchingTvShows(): Flow<List<TvShow>> {
        return watchHistoryLocalDataSource.getTvShowContinueWatching().map {
            tvLocalMapper.toEntityList(it)
        }
    }
}