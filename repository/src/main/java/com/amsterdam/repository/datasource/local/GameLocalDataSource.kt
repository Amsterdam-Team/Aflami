package com.amsterdam.repository.datasource.local

import kotlinx.coroutines.flow.Flow

interface GameLocalDataSource {

    suspend fun upsertPoints(points: Int)
    fun getUserPoints(): Flow<Int>
}