package com.example.repository.repository

import com.example.domain.repository.MovieRepository
import com.example.entity.Movie
import com.example.repository.datasource.local.LocalMovieDataSource
import com.example.repository.datasource.local.LocalRecentSearchDataSource
import com.example.repository.datasource.remote.RemoteMovieDatasource
import com.example.repository.dto.local.relation.SearchWithMovies
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.mapper.local.MovieLocalMapper
import com.example.repository.mapper.local.SearchWithMoviesMapper
import com.example.repository.mapper.remote.RemoteMovieMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock


class MovieRepositoryImpl(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDatasource: RemoteMovieDatasource,
    private val remoteMovieMapper: RemoteMovieMapper,
    private val localMovieMapper: MovieLocalMapper,
) : MovieRepository {
    override suspend fun getMoviesByKeyword(keyword: String): List<Movie> {
        return withContext(dispatcher) {
            val localMovies = recentSearchDatasource.getSearchByKeywordAndSearchType(
                keyword = keyword,
                searchType = SearchType.BY_KEYWORD
            )
            if (localMovies.movies.isNotEmpty()) handleLocalSearchResponse(localMovies)
            val remoteMovies = remoteMovieDataSource.getMoviesByKeyword(keyword)
            val domainMovies = movieRemoteMapper.mapResponseToDomain(remoteMovies)
            async {
                localMovieDataSource.addAllMoviesWithSearchData(
                    movies = domainMovies.map { movieLocalMapper.mapToLocal(it) },
                    searchKeyword = keyword,
                    searchType = SearchType.BY_KEYWORD
                )
            }.await()
            domainMovies
        }
    }

    override suspend fun getMoviesByActor(actorName: String): List<Movie> {
        return withContext(dispatcher) {
            val localMovies = recentSearchDatasource.getSearchByKeywordAndSearchType(
                keyword = actorName,
                searchType = SearchType.BY_ACTOR
            )
            if (localMovies.movies.isNotEmpty()) handleLocalSearchResponse(localMovies)
            val remoteMovies = remoteMovieDataSource.getMoviesByActorName(actorName)
            val domainMovies = movieRemoteMapper.mapResponseToDomain(remoteMovies)
            async {
                localMovieDataSource.addAllMoviesWithSearchData(
                    movies = domainMovies.map { movieLocalMapper.mapToLocal(it) },
                    searchKeyword = actorName,
                    searchType = SearchType.BY_ACTOR
                )
            }.await()
            domainMovies


        }

    }

    override suspend fun getMoviesByCountryIsoCode(countryIsoCode: String): List<Movie> {
        return withContext(dispatcher) {
            val localMovies = recentSearchDatasource.getSearchByKeywordAndSearchType(
                keyword = countryIsoCode,
                searchType = SearchType.BY_COUNTRY
            )

            if (localMovies.movies.isNotEmpty()) handleLocalSearchResponse(localMovies)
            val remoteMovies = remoteMovieDataSource.getMoviesByActorName(countryIsoCode)
            val domainMovies = movieRemoteMapper.mapResponseToDomain(remoteMovies)
            async {
                localMovieDataSource.addAllMoviesWithSearchData(
                    movies = domainMovies.map { movieLocalMapper.mapToLocal(it) },
                    searchKeyword = countryIsoCode,
                    searchType = SearchType.BY_COUNTRY
                )
            }.await()
            domainMovies

        }
    }

    private suspend fun handleLocalSearchResponse(searchWithMovies: SearchWithMovies) {
        if (isSearchExpired(searchWithMovies)) {
            recentSearchDatasource.deleteSearchByExpireDate(searchWithMovies.search.expireDate)
        }
    }

    private fun isSearchExpired(searchWithMovies: SearchWithMovies): Boolean {
        val currentTime = Clock.System.now()
        return currentTime > searchWithMovies.search.expireDate
        val remoteMovies = remoteMovieDatasource
            .getMoviesByCountryIsoCode(countryIsoCode)
            .results
            .map { remoteMovieMapper.mapToDomain(it) }
        return remoteMovies
    }
}