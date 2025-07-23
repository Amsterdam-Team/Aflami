package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.LocalTvShowWithSearchDto
import com.example.repository.dto.local.relation.TvShowWithCategory
import com.example.repository.dto.local.utils.DatabaseConstants
import com.example.repository.dto.local.utils.SearchType

@Dao
interface TvShowDao {
    @Upsert
    suspend fun addAllTvShows(tvShows: List<LocalTvShowDto>)

    @Upsert
    suspend fun insertTvShowSearchMappings(mappings: List<LocalTvShowWithSearchDto>)

    @Transaction
    @Query(
        """
        SELECT * FROM tv_shows 
        WHERE tvShowId IN (
            SELECT tvShowId FROM ${DatabaseConstants.RECENT_SEARCH_TABLE}
            WHERE searchKeyword = :keyword
            AND searchType = :searchType
            AND storedLanguage = :storedLanguage
            LIMIT :limit OFFSET :offset
        )
        """
    )
    suspend fun getTvShowsBySearchKeyword(
        keyword: String, searchType: SearchType,
        storedLanguage: String,
        limit: Int,
        offset: Int
    ): List<TvShowWithCategory>
}