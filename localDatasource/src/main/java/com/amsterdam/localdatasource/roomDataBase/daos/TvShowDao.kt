package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.LocalTvShowWithSearchDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategory

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
            SELECT tvShowId FROM tvshow_search
            WHERE searchKeyword = :keyword
        )
        """
    )
    suspend fun getTvShowsBySearchKeyword(keyword: String): List<TvShowWithCategory>
}