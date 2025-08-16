package com.amsterdam.localdatasource.inMemory

import com.amsterdam.repository.datasource.local.GameSessionLocalDataSource
import javax.inject.Inject

class GameInMemoryDataSource @Inject constructor(): GameSessionLocalDataSource {

    private data class GameSessionStats(
        var spentSeconds: Int = 0,
        var points: Int = 0
    )

    private val sessions = mutableMapOf<Long, GameSessionStats>()

    override fun addOneSecond(gameSessionId: Long) {
        val session = sessions.getOrPut(gameSessionId) { GameSessionStats() }
        session.spentSeconds++
    }

    override fun getTotalSpentSeconds(gameSessionId: Long): Int {
        return sessions[gameSessionId]?.spentSeconds ?: 0
    }

    override fun addPoints(points: Int, gameSessionId: Long) {
        val session = sessions.getOrPut(gameSessionId) { GameSessionStats() }
        session.points += points
    }

    override fun getCollectedPoints(gameSessionId: Long): Int {
        return sessions[gameSessionId]?.points ?: 0
    }

}
