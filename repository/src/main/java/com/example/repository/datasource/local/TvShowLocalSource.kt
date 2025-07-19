package com.example.repository.datasource.local

import com.example.entity.category.MovieGenre
import com.example.entity.category.TvShowGenre
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.relation.TvShowWithCategory
import com.example.repository.dto.local.utils.SearchType

interface TvShowLocalSource {
    suspend fun getTvShowsByKeywordAndSearchType(searchKeyword: String, searchType: SearchType): List<TvShowWithCategory>
    suspend fun addTvShows(tvShows: List<LocalTvShowDto>, searchKeyword: String)

    suspend fun incrementGenreInterest(genre: TvShowGenre)
    suspend fun getAllGenreInterests(): Map<TvShowGenre, Int>
}