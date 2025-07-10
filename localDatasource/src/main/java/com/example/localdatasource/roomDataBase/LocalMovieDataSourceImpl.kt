package com.example.localdatasource.roomDatabase

import com.example.localdatasource.roomDatabase.daos.LocalMovieDao
import com.example.repository.datasource.local.LocalMovieDataSource
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Clock


class LocalMovieDataSourceImpl(
    private val dao: LocalMovieDao
) : LocalMovieDataSource {

    override suspend fun getMoviesByKeywordAndSearchType(
        keyword: String,
        searchType: SearchType,
        rating: Int?,
        category: String?
    ): List<MovieWithCategories> {
        return dao.getMoviesByKeywordAndSearchType(keyword, searchType, rating, category)
    }

    override suspend fun addAllMoviesWithSearchData(
        movies: List<LocalMovieDto>,
        searchKeyword: String,
        searchType: SearchType,
        rating: Int?,
        category: String?
    ) {
        dao.insertMovies(movies)

        val entries = movies.map { movie ->
            LocalSearchDto(
                searchKeyword = searchKeyword,
                searchType = searchType,
                rating = rating,
                movieId = movie.id,
                category = category,
                saveDate = Clock.System.now()
            )
        }

        dao.insertSearchEntries(entries)
    }
}
