package com.amsterdam.repository.datasource.local

import kotlinx.coroutines.flow.Flow

interface GamePointsLocalDataSource {
    suspend fun upsertPoints(points: Int)
    suspend fun getPoints(): Flow<Int?>
}