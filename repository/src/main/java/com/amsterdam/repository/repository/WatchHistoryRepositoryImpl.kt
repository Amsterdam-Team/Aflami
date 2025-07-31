package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.entity.TvShowWatchHistory
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.MovieLocalSource
import com.amsterdam.repository.datasource.local.TvShowLocalSource
import com.amsterdam.repository.datasource.local.WatchHistoryLocalDataSource
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.datasource.remote.TvShowsRemoteSource
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto
import com.amsterdam.repository.dto.remote.RemoteMovieDetailsResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.mapper.local.MovieWatchHistoryLocalMapper
import com.amsterdam.repository.mapper.local.TvWatchHistoryLocalMapper
import com.amsterdam.repository.mapper.remote.MovieDetailRemoteMapper
import com.amsterdam.repository.mapper.remoteToLocal.MovieRemoteLocalMapper
import com.amsterdam.repository.mapper.remoteToLocal.TvShowRemoteDetailsLocalMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WatchHistoryRepositoryImpl @Inject constructor(
    private val watchHistoryLocalDataSource: WatchHistoryLocalDataSource,
    private val movieLocalSource: MovieLocalSource,
    private val movieRemoteDataSource: MovieRemoteSource,
    private val tvShowLocalDataSource: TvShowLocalSource,
    private val tvShowRemoteSource: TvShowsRemoteSource,
    private val preferences: AppPreferences,
    private val tvShowRemoteDetailsLocalMapper: TvShowRemoteDetailsLocalMapper,
    private val localTvDataSource: TvShowLocalSource,
    private val movieWatchHistoryLocalMapper: MovieWatchHistoryLocalMapper,
    private val tvWatchHistoryLocalMapper: TvWatchHistoryLocalMapper,
    private val movieRemoteLocalMapper: MovieRemoteLocalMapper,
    private val movieDetailRemoteMapper: MovieDetailRemoteMapper,
) : WatchHistoryRepository {

    override suspend fun addMovieToWatchHistory(movieId: Long) {
        watchHistoryLocalDataSource.addMovieToWatchHistory(MovieWatchHistoryDto(movieId))
    }

    override fun getContinueWatchingMovies(
        page: Int,
        pageSize: Int
    ): Flow<List<MovieWatchHistory>> = flow {
        watchHistoryLocalDataSource.getMoviesWatchHistory(page, pageSize).collect {
            emit(it.map { getMovieWatchHistory(it) })
        }
    }

    private suspend fun getMovieWatchHistory(movieWatchHistoryDto: MovieWatchHistoryDto): MovieWatchHistory {
        return movieLocalSource.getMovieById(movieWatchHistoryDto.movieId, preferences.getDeviceLanguage().first())
            ?.let {
                movieWatchHistoryLocalMapper.toEntity(it.copy(insertedDate = movieWatchHistoryDto.watchedDate))
            }
            ?: movieRemoteDataSource.getMovieDetailsById(movieWatchHistoryDto.movieId).run {
                cacheWatchedMovie(this)
                movieLocalSource.getMovieById(movieWatchHistoryDto.movieId, preferences.getDeviceLanguage().first())
                    .let {
                        movieWatchHistoryLocalMapper.toEntity(it!!.copy(insertedDate = movieWatchHistoryDto.watchedDate))
                    }
            }
    }

    private suspend fun cacheWatchedMovie(remoteMovieDetailsResponse: RemoteMovieDetailsResponse) {
        movieLocalSource.insertMovie(
            movieRemoteLocalMapper.toLocal(
                remote = movieDetailRemoteMapper.mapMovieDetailsToMovieItemDto(
                    remoteMovieDetailsResponse
                ), args = listOf(preferences.getDeviceLanguage().first())
            )
        )
    }

    override suspend fun addTvShowToWatchHistory(tvShowId: Long) {
        watchHistoryLocalDataSource.addTvShowToWatchHistory(TvShowWatchHistoryDto(tvShowId))
    }

    override fun getContinueWatchingTvShows(
        page: Int,
        pageSize: Int
    ): Flow<List<TvShowWatchHistory>> = flow {
        watchHistoryLocalDataSource.getTvShowsWatchHistory(page, pageSize).collect {
            emit(it.map { getTvShowWatchHistory(it) })
        }
    }


    private suspend fun getTvShowWatchHistory(tvShowWatchHistoryDto: TvShowWatchHistoryDto): TvShowWatchHistory {
        return tvShowLocalDataSource.getTvShowById(
            tvShowWatchHistoryDto.tvShowId,
            preferences.getDeviceLanguage().first()
        )?.let {
            tvWatchHistoryLocalMapper.toEntity(it.copy(insertedDate = tvShowWatchHistoryDto.watchedDate))
        }
            ?: tvShowRemoteSource.getTvShowDetailsById(tvShowWatchHistoryDto.tvShowId).run {
                cacheWatchedTvShow(this)
                tvShowLocalDataSource.getTvShowById(
                    tvShowWatchHistoryDto.tvShowId,
                    preferences.getDeviceLanguage().first()
                ).let {
                    tvWatchHistoryLocalMapper.toEntity(it!!.copy(insertedDate = tvShowWatchHistoryDto.watchedDate))
                }
            }
    }

    private suspend fun cacheWatchedTvShow(remoteTvShowItemDto: TvShowDetailsRemoteResponse) {
        localTvDataSource.insertTvShow(
            tvShowRemoteDetailsLocalMapper.toLocal(
                remote = remoteTvShowItemDto, args = listOf(preferences.getDeviceLanguage().first())
            )
        )
    }
}