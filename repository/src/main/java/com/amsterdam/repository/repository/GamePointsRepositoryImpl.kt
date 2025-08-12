package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.GamePointsRepository
import com.amsterdam.repository.datasource.local.GamePointsLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GamePointsRepositoryImpl @Inject constructor(
    private val gamePointsLocalDataSource: GamePointsLocalDataSource
) : GamePointsRepository {
    override suspend fun updatePoints(points: Int) {
        gamePointsLocalDataSource.upsertPoints(points)
    }

    override fun getPoints(): Flow<Int> {
        return gamePointsLocalDataSource.getPoints().map { nullablePoints ->
            nullablePoints ?: 0
        }
    }
}