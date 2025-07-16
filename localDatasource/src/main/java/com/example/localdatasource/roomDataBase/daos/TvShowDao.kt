package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.LocalTvShowWithSearchDto
import com.example.repository.dto.local.SearchMovieCrossRefDto
import com.example.repository.dto.local.relation.TvShowWithCategory
import com.example.repository.dto.local.utils.DatabaseContract
import com.example.repository.dto.local.utils.SearchType

@Dao
interface TvShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllTvShows(tvShows: List<LocalTvShowDto>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTvShowSearchMappings(mappings: List<LocalTvShowWithSearchDto>)

    @Transaction
    @Query(
        """
        SELECT * FROM tv_shows 
        WHERE tvShowId IN (
            SELECT tvShowId FROM ${DatabaseContract.TV_SHOW_SEARCH_TABLE}
            WHERE searchKeyword = :keyword
        )
        """
    )
    suspend fun getTvShowsBySearchKeyword(keyword: String): List<TvShowWithCategory>
}