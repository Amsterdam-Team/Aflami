package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.GamePointsDao
import com.amsterdam.repository.datasource.local.GameLocalDataSource
import com.amsterdam.repository.dto.local.GamePointsDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GameLocalDataSourceImpl @Inject constructor(private val gamePointsDao: GamePointsDao
) : GameLocalDataSource {
    override suspend fun upsertPoints(points: Int) {
        gamePointsDao.upsertPoints(GamePointsDto(points = points))
    }

    override fun getUserPoints(): Flow<Int> {
        return gamePointsDao.getPoints()
    }

}