package com.example.localdatasource.roomDataBase.datasource

import com.example.localdatasource.roomDataBase.daos.MovieDao
import com.example.repository.datasource.local.MovieLocalSource
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.SearchMovieCrossRefDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant


class MovieLocalDataSourceImpl(
    private val dao: MovieDao
) : MovieLocalSource {

    override suspend fun getMoviesByKeywordAndSearchType(
        keyword: String,
        searchType: SearchType
    ): List<MovieWithCategories> {
        return dao.getMoviesByKeywordAndSearchType(keyword, searchType)
    }

    override suspend fun addMoviesBySearchData(
        movies: List<LocalMovieDto>,
        searchKeyword: String,
        searchType: SearchType,
        expireDate: Instant
    ) {
        dao.insertMovies(movies)

        val entries = movies.map { movie ->
            SearchMovieCrossRefDto(
                searchKeyword = searchKeyword,
                searchType = searchType,
                movieId = movie.movieId
            )
        }

        dao.insertSearchEntries(entries)
    }

    override suspend fun getSearchMovieCrossRefs(
        searchKeyword: String,
        searchType: SearchType
    ): List<SearchMovieCrossRefDto> {
        return dao.getSearchMoviesCrossRef(searchKeyword, searchType)
    }

}
