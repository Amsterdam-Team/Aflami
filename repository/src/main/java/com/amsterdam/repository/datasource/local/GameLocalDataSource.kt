package com.amsterdam.repository.datasource.local

interface GameLocalDataSource {

    suspend fun addGamePoints(points: Int)
    suspend fun getTotalGamePoints(): Int
}