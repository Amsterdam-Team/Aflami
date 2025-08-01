package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.WatchHistoryDao
import com.amsterdam.repository.datasource.local.WatchHistoryLocalDataSource
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WatchHistoryLocalDataSourceImpl @Inject constructor(
    private val dao: WatchHistoryDao
) : WatchHistoryLocalDataSource {

    override suspend fun addMovieToWatchHistory(item: MovieWatchHistoryDto) =
        dao.addMovieToWatchHistory(item)

    override fun getMoviesWatchHistory(page: Int, pageSize: Int) : Flow<List<MovieWatchHistoryDto>> {
        val offset = (page -1) * pageSize
       return dao.getMoviesWatchHistory(offset,pageSize)
    }

    override suspend fun addTvShowToWatchHistory(item: TvShowWatchHistoryDto) =
        dao.addTvShowToWatchHistory(item)

    override fun getTvShowsWatchHistory(page: Int, pageSize: Int): Flow<List<TvShowWatchHistoryDto>> {
        val offset = (page -1) * pageSize
        return dao.getTvShowsWatchHistory(offset,pageSize)
    }
}

