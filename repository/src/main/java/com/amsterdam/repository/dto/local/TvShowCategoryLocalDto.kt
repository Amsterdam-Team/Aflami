package com.amsterdam.repository.dto.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.TV_SHOW_CATEGORY_TABLE
)
data class TvShowCategoryLocalDto(
    @PrimaryKey val categoryId: Long,
)