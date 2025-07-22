package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.entity.category.MovieGenre
import com.example.repository.dto.local.LocalMovieCategoryInterestDto
import com.example.repository.dto.local.utils.DatabaseConstants

@Dao
interface MovieCategoryInterestDao {

    @Query("SELECT * FROM ${DatabaseConstants.MOVIE_CATEGORY_INTEREST_TABLE}")
    suspend fun getAllInterests(): List<LocalMovieCategoryInterestDto>

    @Query("SELECT interestCount FROM ${DatabaseConstants.MOVIE_CATEGORY_INTEREST_TABLE} WHERE genre = :genre")
    suspend fun getInterestCount(genre: MovieGenre): Int?

    @Upsert
    suspend fun insertInterest(entity: LocalMovieCategoryInterestDto)

    @Transaction
    suspend fun incrementInterest(genre: MovieGenre) {
        val current = getInterestCount(genre) ?: 0
        insertInterest(LocalMovieCategoryInterestDto(genre, current + 1))
    }
}
