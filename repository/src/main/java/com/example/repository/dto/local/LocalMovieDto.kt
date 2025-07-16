package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class LocalMovieDto(
    @PrimaryKey(autoGenerate = false) val movieId: Long,
    val name: String,
    val description: String,
    val poster: String,
    val productionYear: Int,
    val popularity: Double,
    val rating: Float,
    val originCountry: String,
    val movieLength: Int,
    val hasVideo : Boolean
)
