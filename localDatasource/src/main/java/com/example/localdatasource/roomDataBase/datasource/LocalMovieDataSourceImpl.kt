package com.example.localdatasource.roomDataBase.datasource

import com.example.localdatasource.roomDataBase.daos.MovieDao
import com.example.repository.datasource.local.LocalMovieDataSource
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.dto.local.utils.SearchType


class LocalMovieDataSourceImpl(
    private val dao: MovieDao
) : LocalMovieDataSource {

    override suspend fun getMoviesByKeywordAndSearchType(
        keyword: String,
        searchType: SearchType
    ): List<MovieWithCategories> {
        return dao.getMoviesByKeywordAndSearchType(keyword, searchType)
    }

    override suspend fun addAllMoviesWithSearchData(
        movies: List<LocalMovieDto>,
        searchKeyword: String,
        searchType: SearchType
    ) {
        dao.insertMovies(movies)

        val entries = movies.map { movie ->
//            LocalSearchDto(
//                searchKeyword = searchKeyword,
//                searchType = searchType,
//                rating = rating,
//                category = category,
//                expireDate = Clock.System.now().plus(1.hours)
//            )
        }

//        dao.insertSearchEntries(entries)
    }
}
