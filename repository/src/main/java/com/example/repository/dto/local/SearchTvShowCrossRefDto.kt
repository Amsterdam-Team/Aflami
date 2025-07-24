package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.repository.dto.local.utils.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.TV_SHOW_SEARCH_TABLE,
    primaryKeys = ["tvShowId", "searchKeyword", "storedLanguage"],
    foreignKeys = [
        ForeignKey(
            entity = LocalTvShowDto::class,
            parentColumns = ["tvShowId", "storedLanguage"],
            childColumns = ["tvShowId", "storedLanguage"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SearchTvShowCrossRefDto(
    val searchKeyword: String,
    val storedLanguage: String,
    val tvShowId: Long,
)
