package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class LocalMovieDto(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String,
    val description: String,
    val poster: String,
    val productionYear: Int,
    val rating: Float,
)
