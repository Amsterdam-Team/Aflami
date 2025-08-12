package com.amsterdam.domain.repository

import kotlinx.coroutines.flow.Flow

interface GamePointsRepository {
    suspend fun updatePoints(points: Int)
    fun getPoints(): Flow<Int>
}