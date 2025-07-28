package com.amsterdam.localdatasource.roomDataBase.datasource

import android.util.Log
import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.entity.TvShowWatchHistory
import com.amsterdam.localdatasource.roomDataBase.daos.WatchHistoryDao
import com.amsterdam.repository.datasource.local.WatchHistoryLocalDataSource
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WatchHistoryLocalDataSourceImpl @Inject constructor(
    private val dao: WatchHistoryDao
) : WatchHistoryLocalDataSource {

    override suspend fun addMovieToWatchHistory(item: MovieWatchHistoryDto) =
        dao.addMovieToWatchHistory(item)

    override fun getMovieContinueWatching(): Flow<List<LocalMovieDto>> =
        dao.getMovieContinueWatching()

    override suspend fun addTvShowToWatchHistory(item: TvShowWatchHistoryDto) =
        dao.addTvShowToWatchHistory(item)

    override fun getTvShowContinueWatching(): Flow<List<LocalTvShowDto>> {
        return dao.getTvShowContinueWatching()
    }
}

