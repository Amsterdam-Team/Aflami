package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.GamePointsDto
import com.amsterdam.repository.dto.local.utils.DatabaseConstants
import kotlinx.coroutines.flow.Flow

@Dao
interface GamePointsDao {
    @Upsert
    suspend fun upsertPoints(gamePoints: GamePointsDto)

    @Query("SELECT points FROM ${DatabaseConstants.GAME_POINTS_TABLE} LIMIT 1")
    fun getPoints(): Flow<Int>
}