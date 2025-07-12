package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.relation.TvShowWithCategories
import com.example.repository.dto.local.utils.SearchType

@Dao
interface TvShowDao {

    @Transaction
    @Query(
        """
        SELECT * FROM tv_shows 
        WHERE tvId IN (
            SELECT tvId FROM SearchDto 
            WHERE searchKeyword = :keyword 
              AND searchType = :searchType
        )
        """
    )
    suspend fun getTvShowsByKeywordAndSearchType(
        keyword: String,
        searchType: SearchType
    ): List<TvShowWithCategories>

    @Upsert
    suspend fun insertTvShows(movies: List<LocalTvShowDto>)

    @Upsert
    suspend fun insertSearchEntries(entries: List<LocalSearchDto>)
}