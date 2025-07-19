package com.example.repository.datasource.local

import com.example.entity.category.MovieGenre
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.SearchMovieCrossRefDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant

interface MovieLocalSource {
    suspend fun getMoviesByKeywordAndSearchType(
        keyword: String,
        searchType: SearchType
    ): List<MovieWithCategories>

    suspend fun addMoviesBySearchData(
        movies: List<LocalMovieDto>,
        searchKeyword: String,
        searchType: SearchType,
        expireDate: Instant
    )

    suspend fun getSearchMovieCrossRefs(
        searchKeyword: String,
        searchType: SearchType,
    ): List<SearchMovieCrossRefDto>

    suspend fun getMovieById(movieId : Long) : LocalMovieDto

    suspend fun incrementGenreInterest(genre: MovieGenre)
    suspend fun getAllGenreInterests(): Map<MovieGenre, Int>
}