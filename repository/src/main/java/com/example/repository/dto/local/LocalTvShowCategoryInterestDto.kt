package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.entity.category.TvShowGenre
import com.example.repository.dto.local.utils.DatabaseContract

@Entity(tableName = DatabaseContract.TV_SHOW_CATEGORY_INTEREST_TABLE)
data class LocalTvShowCategoryInterestDto(
    @PrimaryKey val genre: TvShowGenre,
    val interestCount: Int
)