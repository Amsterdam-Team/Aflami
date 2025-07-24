package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.repository.dto.local.LocalTvShowCategoryInterestDto
import com.example.repository.dto.local.utils.DatabaseConstants

@Dao
interface TvShowCategoryInterestDao {
    @Upsert
    suspend fun insertInterest(entity: LocalTvShowCategoryInterestDto)

    @Query("SELECT interestCount FROM ${DatabaseConstants.TV_SHOW_CATEGORY_INTEREST_TABLE} WHERE categoryId = :categoryId")
    suspend fun getInterestCount(categoryId: Long): Int?

    @Transaction
    suspend fun incrementInterest(categoryId: Long) {
        val current = getInterestCount(categoryId) ?: 0
        insertInterest(LocalTvShowCategoryInterestDto(categoryId, current + 1))
    }
}