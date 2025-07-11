package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.LocalTvShowWithSearchDto

@Dao
interface TvShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllTvShows(tvShows: List<LocalTvShowDto>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTvShowSearchMappings(mappings: List<LocalTvShowWithSearchDto>)

    @Query(
        """
        SELECT * FROM LocalTvShowDto 
        WHERE id IN (
            SELECT tvShowId FROM tvshow_search
            WHERE searchKeyword = :keyword
        )
        """
    )
    suspend fun getTvShowsBySearchKeyword(keyword: String): List<LocalTvShowDto>
}