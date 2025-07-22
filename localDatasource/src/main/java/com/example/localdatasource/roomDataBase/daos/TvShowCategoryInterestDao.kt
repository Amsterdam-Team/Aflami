package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.entity.category.TvShowGenre
import com.example.repository.dto.local.LocalTvShowCategoryInterestDto
import com.example.repository.dto.local.utils.DatabaseConstants

@Dao
interface TvShowCategoryInterestDao {

    @Query("SELECT * FROM ${DatabaseConstants.TV_SHOW_CATEGORY_INTEREST_TABLE}")
    suspend fun getAllInterests(): List<LocalTvShowCategoryInterestDto>

    @Query("SELECT interestCount FROM ${DatabaseConstants.TV_SHOW_CATEGORY_INTEREST_TABLE} WHERE genre = :genre")
    suspend fun getInterestCount(genre: TvShowGenre): Int?

    @Upsert
    suspend fun insertInterest(entity: LocalTvShowCategoryInterestDto)

    @Transaction
    suspend fun incrementInterest(genre: TvShowGenre) {
        val current = getInterestCount(genre) ?: 0
        insertInterest(LocalTvShowCategoryInterestDto(genre, current + 1))
    }
}