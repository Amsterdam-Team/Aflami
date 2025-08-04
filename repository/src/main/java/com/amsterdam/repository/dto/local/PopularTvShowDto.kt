package com.amsterdam.repository.dto.local

import androidx.room.Entity
import androidx.room.ForeignKey
import com.amsterdam.repository.dto.local.utils.DatabaseConstants
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(
    tableName = DatabaseConstants.POPULAR_TV_SHOW_TABLE,
    primaryKeys = ["tvShowId", "storedLanguage"],
    foreignKeys = [
        ForeignKey(
            entity = LocalTvShowDto::class,
            parentColumns = ["tvShowId", "storedLanguage"],
            childColumns = ["tvShowId", "storedLanguage"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
)
data class PopularTvShowDto(
    val tvShowId: Long,
    val storedLanguage: String,
    val dateAdded: Instant = Clock.System.now()
)
