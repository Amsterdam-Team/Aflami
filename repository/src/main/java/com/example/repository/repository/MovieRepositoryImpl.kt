package com.example.repository.repository

import com.example.domain.repository.MovieRepository
import com.example.entity.Movie
import com.example.repository.datasource.local.LocalMovieDataSource
import com.example.repository.datasource.local.LocalRecentSearchDataSource
import com.example.repository.datasource.remote.RemoteMovieDatasource
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.mapper.local.MovieLocalMapper
import com.example.repository.mapper.remote.RemoteMovieMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class MovieRepositoryImpl(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDatasource,
    private val movieLocalMapper: MovieLocalMapper,
    private val movieRemoteMapper: RemoteMovieMapper,
    private val recentSearchDatasource: LocalRecentSearchDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieRepository {
    override suspend fun getMoviesByKeyword(keyword: String): List<Movie> {

        val recentSearch =
            recentSearchDatasource.getSearchByKeywordAndType(keyword, SearchType.BY_KEYWORD)
        val isExpired = !isSearchExpired(recentSearch)
        if (!isExpired) {
            val localMovies = localMovieDataSource.getMoviesByKeywordAndSearchType(
                keyword = keyword,
                searchType = SearchType.BY_KEYWORD
            )
            return movieLocalMapper.mapListFromLocal(localMovies)
        }
        deleteRecentSearch(recentSearch)
        val remoteMovies = remoteMovieDataSource.getMoviesByKeyword(keyword)
        val domainMovies = movieRemoteMapper.mapResponseToDomain(remoteMovies)

        localMovieDataSource.addAllMoviesWithSearchData(
            movies = domainMovies.map { movieLocalMapper.mapToLocal(it) },
            searchKeyword = keyword,
            searchType = SearchType.BY_KEYWORD,
            expireDate = Clock.System.now()
        )

        return domainMovies
    }

    override suspend fun getMoviesByActor(actorName: String): List<Movie> {
        return withContext(dispatcher) {
            val recentSearch =
                recentSearchDatasource.getSearchByKeywordAndType(actorName, SearchType.BY_ACTOR)
            val isExpired = !isSearchExpired(recentSearch)
            if (!isExpired) {
                val localMovies = localMovieDataSource.getMoviesByKeywordAndSearchType(
                    keyword = actorName,
                    searchType = SearchType.BY_ACTOR
                )
                return@withContext movieLocalMapper.mapListFromLocal(localMovies)
            }
            deleteRecentSearch(recentSearch)
            val remoteMovies = remoteMovieDataSource.getMoviesByActorName(actorName)
            val domainMovies = movieRemoteMapper.mapResponseToDomain(remoteMovies)

            launch {
                localMovieDataSource.addAllMoviesWithSearchData(
                    movies = domainMovies.map { movieLocalMapper.mapToLocal(it) },
                    searchKeyword = actorName,
                    searchType = SearchType.BY_ACTOR,
                    expireDate = Clock.System.now()
                )
            }
            domainMovies
        }

    }

    override suspend fun getMoviesByCountryIsoCode(countryIsoCode: String): List<Movie> {
        return withContext(dispatcher) {
            val recentSearch =
                recentSearchDatasource.getSearchByKeywordAndType(
                    countryIsoCode,
                    SearchType.BY_COUNTRY
                )
            val isExpired = !isSearchExpired(recentSearch)
            if (!isExpired) {
                val localMovies = localMovieDataSource.getMoviesByKeywordAndSearchType(
                    keyword = countryIsoCode,
                    searchType = SearchType.BY_COUNTRY
                )
                return@withContext movieLocalMapper.mapListFromLocal(localMovies)
            }
            deleteRecentSearch(recentSearch)
            val remoteMovies = remoteMovieDataSource.getMoviesByCountryIsoCode(countryIsoCode)
            val domainMovies = movieRemoteMapper.mapResponseToDomain(remoteMovies)

            launch {
                localMovieDataSource.addAllMoviesWithSearchData(
                    movies = domainMovies.map { movieLocalMapper.mapToLocal(it) },
                    searchKeyword = countryIsoCode,
                    searchType = SearchType.BY_COUNTRY,
                    expireDate = Clock.System.now()
                )
            }
            domainMovies
        }
    }

    private suspend fun deleteRecentSearch(
        recentSearch: LocalSearchDto?
    ) {
        recentSearchDatasource.deleteSearchByKeywordAndType(
            recentSearch?.searchKeyword ?: "",
            recentSearch?.searchType ?: SearchType.BY_KEYWORD
        )

    }

    private fun isSearchExpired(recentSearch: LocalSearchDto?): Boolean {
        return recentSearch?.expireDate != null && recentSearch.expireDate < Clock.System.now()
    }
}