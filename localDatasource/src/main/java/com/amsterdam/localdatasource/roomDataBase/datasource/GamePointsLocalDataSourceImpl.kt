package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.GamePointsDao
import com.amsterdam.repository.datasource.local.GamePointsLocalDataSource
import com.amsterdam.repository.dto.local.GamePointsDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GamePointsLocalDataSourceImpl @Inject constructor(
    private val gamePointsDao: GamePointsDao
) : GamePointsLocalDataSource {
    override suspend fun upsertPoints(points: Int) {
        gamePointsDao.upsertPoints(GamePointsDto(points = points))
    }

    override fun getPoints(): Flow<Int?> {
        return gamePointsDao.getPoints()
    }
}