package com.amsterdam.repository.dto.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Entity(tableName = DatabaseConstants.GAME_POINTS_TABLE)
data class GamePointsDto(
    @PrimaryKey(autoGenerate = false) val id: Int = 1,
    val points: Int
)