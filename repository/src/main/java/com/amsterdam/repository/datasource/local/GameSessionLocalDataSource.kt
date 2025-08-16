package com.amsterdam.repository.datasource.local

interface GameSessionLocalDataSource {
    fun addOneSecond(gameSessionId: Long)
    fun getTotalSpentSeconds(gameSessionId: Long): Int
    fun addPoints(points: Int, gameSessionId: Long)
    fun getCollectedPoints(gameSessionId: Long): Int
}