package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.repository.dto.local.utils.DatabaseContract

@Entity(
    tableName = DatabaseContract.TV_SHOW_TABLE
)
data class LocalTvShowDto(
    @PrimaryKey(autoGenerate = false) val tvShowId: Long,
    val name: String,
    val description: String,
    val poster: String,
    val productionYear: Int,
    val rating: Float,
    val popularity: Double
)