package com.example.repository.repository

import com.example.domain.repository.MovieRepository
import com.example.domain.useCase.genreTypes.MovieGenre
import com.example.entity.Movie
import com.example.repository.datasource.local.MovieLocalSource
import com.example.repository.datasource.local.RecentSearchLocalSource
import com.example.repository.datasource.remote.MovieRemoteSource
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.mapper.local.MovieLocalMapper
import com.example.repository.mapper.remote.GenreMapper
import com.example.repository.mapper.remote.RemoteMovieMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class MovieRepositoryImpl(
    private val movieLocalSource: MovieLocalSource,
    private val movieDataSource: MovieRemoteSource,
    private val movieLocalMapper: MovieLocalMapper,
    private val movieRemoteMapper: RemoteMovieMapper,
    private val genreMapper: GenreMapper,
    private val recentSearchDatasource: RecentSearchLocalSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieRepository {
    override suspend fun getMoviesByKeyword(keyword: String, rating: Float, movieGenre: MovieGenre): List<Movie> {

        val recentSearch =
            recentSearchDatasource.getSearchByKeywordAndType(keyword, SearchType.BY_KEYWORD)
        val isExpired = !isSearchExpired(recentSearch)
        if (!isExpired) {
            val localMovies = movieLocalSource.getMoviesByKeywordAndSearchType(
                keyword = keyword,
                searchType = SearchType.BY_KEYWORD
            )
            return movieLocalMapper.mapToMovies(localMovies)
        }
        deleteRecentSearch(recentSearch)

        val remoteMovies = if (rating != 0f || movieGenre != MovieGenre.ALL) {
            movieDataSource.discoverMovies(keyword, rating, genreMapper.mapToMovieGenreId(movieGenre))
        } else {
            movieDataSource.getMoviesByKeyword(keyword)
        }

        val domainMovies = movieRemoteMapper.mapToMovies(remoteMovies)

        movieLocalSource.addMoviesBySearchData(
            movies = domainMovies.map { movieLocalMapper.mapToLocalMovie(it) },
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
                val localMovies = movieLocalSource.getMoviesByKeywordAndSearchType(
                    keyword = actorName,
                    searchType = SearchType.BY_ACTOR
                )
                return@withContext movieLocalMapper.mapToMovies(localMovies)
            }
            deleteRecentSearch(recentSearch)
            val remoteMovies = movieDataSource.getMoviesByActorName(actorName)
            val domainMovies = movieRemoteMapper.mapToMovies(remoteMovies)

            launch {
                movieLocalSource.addMoviesBySearchData(
                    movies = domainMovies.map { movieLocalMapper.mapToLocalMovie(it) },
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
                val localMovies = movieLocalSource.getMoviesByKeywordAndSearchType(
                    keyword = countryIsoCode,
                    searchType = SearchType.BY_COUNTRY
                )
                return@withContext movieLocalMapper.mapToMovies(localMovies)
            }
            deleteRecentSearch(recentSearch)
            val remoteMovies = movieDataSource.getMoviesByCountryIsoCode(countryIsoCode)
            val domainMovies = movieRemoteMapper.mapToMovies(remoteMovies)

            launch {
                movieLocalSource.addMoviesBySearchData(
                    movies = domainMovies.map { movieLocalMapper.mapToLocalMovie(it) },
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
        recentSearchDatasource.deleteRecentSearchByKeywordAndType(
            recentSearch?.searchKeyword ?: "",
            recentSearch?.searchType ?: SearchType.BY_KEYWORD
        )

    }

    private fun isSearchExpired(recentSearch: LocalSearchDto?): Boolean {
        return recentSearch?.expireDate != null && recentSearch.expireDate < Clock.System.now()
    }
}