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
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto
import com.amsterdam.repository.dto.remote.RemoteMovieDetailsResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.mapper.local.toWatchHistoryEntity
import com.amsterdam.repository.mapper.remoteToLocal.toLocalDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WatchHistoryRepositoryImpl @Inject constructor(
    private val watchHistoryLocalDataSource : WatchHistoryLocalDataSource,
    private val movieLocalSource : MovieLocalSource,
    private val movieRemoteDataSource : MovieRemoteSource,
    private val tvShowLocalDataSource : TvShowLocalSource,
    private val tvShowRemoteSource : TvShowsRemoteSource,
    private val preferences : AppPreferences,
    private val localTvDataSource : TvShowLocalSource
) : WatchHistoryRepository {

    override suspend fun addMovieToWatchHistory(movieId: Long) {
        watchHistoryLocalDataSource.addMovieToWatchHistory(MovieWatchHistoryDto(movieId))
    }

    override fun getContinueWatchingMovies(
        page: Int, pageSize: Int
    ): Flow<List<MovieWatchHistory>> = flow {
        runCatching {
            watchHistoryLocalDataSource.getMoviesWatchHistory(page, pageSize).collect {
                it.map { getMovieWatchHistory(it) }.also { emit(it) }
            }
        }
    }

    private suspend fun getMovieWatchHistory(movieWatchHistoryDto: MovieWatchHistoryDto): MovieWatchHistory {
        val language = preferences.getAppLanguage().first()
        return movieWatchHistoryDto.toWatchHistoryEntity(
            getMovieByIdFromLocal(movieWatchHistoryDto.movieId, language)
                ?: fetchAndCacheRemoteMovie(movieWatchHistoryDto.movieId, language)
        )
    }

    private suspend fun getMovieByIdFromLocal(movieId: Long, language: String) =
        movieLocalSource.getMovieById(movieId, language)

    private suspend fun fetchAndCacheRemoteMovie(movieId: Long, language: String): LocalMovieDto {
        return movieRemoteDataSource.getMovieDetailsById(movieId)
            .also { cacheWatchedMovie(it) }
            .toLocalDto(language)
    }

    private suspend fun cacheWatchedMovie(remoteMovieDetailsResponse: RemoteMovieDetailsResponse) {
        movieLocalSource.insertMovie(
            remoteMovieDetailsResponse.toLocalDto(preferences.getAppLanguage().first())
        )
    }

    override suspend fun addTvShowToWatchHistory(tvShowId: Long) {
        watchHistoryLocalDataSource.addTvShowToWatchHistory(TvShowWatchHistoryDto(tvShowId))
    }

    override fun getContinueWatchingTvShows(
        page: Int, pageSize: Int
    ): Flow<List<TvShowWatchHistory>> = flow {
        runCatching {
            watchHistoryLocalDataSource.getTvShowsWatchHistory(page, pageSize).collect {
                it.map { getTvShowWatchHistory(it) }.also { emit(it) }
            }
        }
    }

    private suspend fun getTvShowWatchHistory(tvShowWatchHistoryDto: TvShowWatchHistoryDto): TvShowWatchHistory {
        val language = preferences.getAppLanguage().first()

        return tvShowWatchHistoryDto.toWatchHistoryEntity(
            getTvShowByIdFromLocal(tvShowWatchHistoryDto.tvShowId, language)
                ?: fetchAndCacheRemoteTvShow(tvShowWatchHistoryDto.tvShowId, language)
        )
    }

    private suspend fun getTvShowByIdFromLocal(tvShowId: Long, language: String) =
        tvShowLocalDataSource.getTvShowById(tvShowId, language)

    private suspend fun fetchAndCacheRemoteTvShow(movieId: Long, language: String): LocalTvShowDto {
        return tvShowRemoteSource.getTvShowDetailsById(movieId)
            .also { cacheWatchedTvShow(it) }
            .toLocalDto(language)
    }

    private suspend fun cacheWatchedTvShow(remoteTvShowItemDto: TvShowDetailsRemoteResponse) {
        localTvDataSource.insertTvShow(
            tvShow = remoteTvShowItemDto.toLocalDto(
                preferences.getAppLanguage().first()
            )
        )
    }
}