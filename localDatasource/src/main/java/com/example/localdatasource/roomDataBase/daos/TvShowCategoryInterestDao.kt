package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.entity.category.TvShowGenre
import com.example.repository.dto.local.LocalTvShowCategoryInterestDto
import com.example.repository.dto.local.utils.DatabaseContract

@Dao
interface TvShowCategoryInterestDao {

    @Query("SELECT * FROM ${DatabaseContract.TV_SHOW_CATEGORY_INTEREST_TABLE}")
    suspend fun getAllInterests(): List<LocalTvShowCategoryInterestDto>

    @Query("SELECT ${DatabaseContract.INTEREST_COUNT_FIELD} FROM ${DatabaseContract.TV_SHOW_CATEGORY_INTEREST_TABLE} WHERE ${DatabaseContract.GENRE_FIELD} = :genre")
    suspend fun getInterestCount(genre: TvShowGenre): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInterest(entity: LocalTvShowCategoryInterestDto)

    @Transaction
    suspend fun incrementInterest(genre: TvShowGenre) {
        val current = getInterestCount(genre) ?: 0
        insertInterest(LocalTvShowCategoryInterestDto(genre, current + 1))
    }
}