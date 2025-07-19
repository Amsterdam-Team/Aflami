package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.entity.category.MovieGenre
import com.example.repository.dto.local.LocalMovieCategoryInterestDto
import com.example.repository.dto.local.utils.DatabaseContract

@Dao
interface MovieCategoryInterestDao {

    @Query("SELECT * FROM ${DatabaseContract.MOVIE_CATEGORY_INTEREST_TABLE}")
    suspend fun getAllInterests(): List<LocalMovieCategoryInterestDto>

    @Query("SELECT ${DatabaseContract.INTEREST_COUNT_FIELD} FROM ${DatabaseContract.MOVIE_CATEGORY_INTEREST_TABLE} WHERE ${DatabaseContract.GENRE_FIELD} = :genre")
    suspend fun getInterestCount(genre: MovieGenre): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInterest(entity: LocalMovieCategoryInterestDto)

    @Transaction
    suspend fun incrementInterest(genre: MovieGenre) {
        val current = getInterestCount(genre) ?: 0
        insertInterest(LocalMovieCategoryInterestDto(genre, current + 1))
    }
}
