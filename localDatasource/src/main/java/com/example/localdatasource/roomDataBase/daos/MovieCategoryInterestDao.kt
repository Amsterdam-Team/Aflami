package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.repository.dto.local.LocalMovieCategoryInterestDto
import com.example.repository.dto.local.utils.DatabaseConstants

@Dao
interface MovieCategoryInterestDao {

    @Query("SELECT interestCount FROM ${DatabaseConstants.MOVIE_CATEGORY_INTEREST_TABLE} WHERE categoryId = :categoryId")
    suspend fun getInterestCount(categoryId: Long): Int?

    @Upsert
    suspend fun insertInterest(entity: LocalMovieCategoryInterestDto)

    @Transaction
    suspend fun incrementInterest(categoryId: Long) {
        val current = getInterestCount(categoryId) ?: 0
        insertInterest(LocalMovieCategoryInterestDto(categoryId, current + 1))
    }
}
