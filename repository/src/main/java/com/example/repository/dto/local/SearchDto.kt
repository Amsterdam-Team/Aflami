package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant

@Entity(
    tableName = "SearchDto",
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
data class SearchDto(
    val searchKeyword: String,
    val searchType: SearchType,
    val rating: Int?,
    val category: String?,
    val movieId: Long,
    val saveDate: Instant
)

