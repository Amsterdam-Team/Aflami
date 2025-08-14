package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.MovieCategoryInterestLocalDto
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Dao
interface MovieCategoryInterestDao {

    @Query("SELECT interestCount FROM ${DatabaseConstants.MOVIE_CATEGORY_INTEREST_TABLE} WHERE categoryId = :categoryId")
    suspend fun getInterestCount(categoryId: Long): Int?

    @Upsert
    suspend fun upsertInterest(entity: MovieCategoryInterestLocalDto)

    @Transaction
    suspend fun incrementInterest(categoryId: Long) {
        val current = getInterestCount(categoryId) ?: 0
        upsertInterest(MovieCategoryInterestLocalDto(categoryId, current + 1))
    }
}
