package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.repository.dto.local.utils.SearchType
import java.time.Instant

@Entity(
    tableName = "search",
    foreignKeys = [
        ForeignKey(
            entity = LocalMovieDto::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("movieId")]
)
data class LocalSearchDto(
    val searchKeyword: String,
    val searchType: SearchType,
    val rating:Int? = null,
    val movieId: Long,
    val saveDate: Instant
)