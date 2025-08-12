package com.amsterdam.repository.datasource.local

import kotlinx.coroutines.flow.Flow

interface GamePointsLocalDataSource {
    suspend fun upsertPoints(points: Int)
    fun getPoints(): Flow<Int?>
}