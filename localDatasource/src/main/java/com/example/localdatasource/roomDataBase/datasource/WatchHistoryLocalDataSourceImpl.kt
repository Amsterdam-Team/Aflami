package com.example.localdatasource.roomDataBase.datasource

import com.example.localdatasource.roomDataBase.daos.WatchHistoryDao
import com.example.repository.datasource.local.WatchHistoryLocalDataSource
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.WatchHistoryDto
import kotlinx.coroutines.flow.Flow

class WatchHistoryLocalDataSourceImpl(
    private val dao: WatchHistoryDao
) : WatchHistoryLocalDataSource {

    override suspend fun addToWatchHistory(item: WatchHistoryDto) =
        dao.addToWatchHistory(item)


    override suspend fun getContinueWatching(): Flow<List<LocalMovieDto>> =
         dao.getContinueWatching()

}
