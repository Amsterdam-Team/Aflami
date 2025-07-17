package com.example.repository.repository

import com.example.domain.repository.MovieRepository
import com.example.repository.datasource.local.MovieLocalSource
import com.example.repository.datasource.remote.MovieRemoteSource
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.dto.remote.RemoteMovieResponse
import com.example.repository.mapper.local.MovieLocalMapper
import com.example.repository.mapper.remote.MovieRemoteMapper
import com.example.repository.utils.RecentSearchHandler
import kotlinx.datetime.Clock

class MovieRepositoryImpl(
    private val movieLocalSource: MovieLocalSource,
    private val movieDataSource: MovieRemoteSource,
    private val movieLocalMapper: MovieLocalMapper,
    private val movieRemoteMapper: MovieRemoteMapper,
    private val recentSearchHandler: RecentSearchHandler,
) : MovieRepository {
    override suspend fun getMoviesByKeyword(keyword: String) =
        getCachedMovies(keyword, SearchType.BY_KEYWORD) ?: recentSearchHandler.deleteRecentSearch(
            keyword,
            SearchType.BY_KEYWORD
        ).let { getMoviesByKeywordFromRemote(keyword, SearchType.BY_KEYWORD) }

    override suspend fun getMoviesByActor(actorName: String) =
        getCachedMovies(actorName, SearchType.BY_ACTOR) ?: recentSearchHandler.deleteRecentSearch(
            actorName,
            SearchType.BY_ACTOR
        ).let { getMoviesByActorNameFromRemote(actorName, SearchType.BY_ACTOR) }

    override suspend fun getMoviesByCountryIsoCode(countryIsoCode: String) =
        getCachedMovies(countryIsoCode, SearchType.BY_COUNTRY)
            ?: recentSearchHandler.deleteRecentSearch(countryIsoCode, SearchType.BY_COUNTRY)
                .let { getMoviesByCountryIsoCodeFromRemote(countryIsoCode, SearchType.BY_COUNTRY) }

    private suspend fun getCachedMovies(keyword: String, searchType: SearchType) =
        recentSearchHandler.isRecentSearchExpired(keyword, searchType)
            .takeIf { isRecentSearchExpired -> !isRecentSearchExpired }
            ?.let { getMoviesFromLocal(keyword, searchType) }
            ?.takeIf { movies -> movies.isNotEmpty() }

    private suspend fun getMoviesByKeywordFromRemote(
        keyword: String, searchType: SearchType
    ) = onSuccessGetMovies(movieDataSource.getMoviesByKeyword(keyword), keyword, searchType)

    private suspend fun getMoviesByActorNameFromRemote(
        actorName: String, searchType: SearchType
    ) = onSuccessGetMovies(movieDataSource.getMoviesByActorName(actorName), actorName, searchType)

    private suspend fun getMoviesByCountryIsoCodeFromRemote(
        countryIsoCode: String, searchType: SearchType
    ) = onSuccessGetMovies(
        movieDataSource.getMoviesByCountryIsoCode(countryIsoCode), countryIsoCode, searchType
    )

    private suspend fun onSuccessGetMovies(
        remoteMovies: RemoteMovieResponse, actorName: String, searchType: SearchType
    ) = saveMoviesWithSearch(
        remoteMovies, actorName, searchType
    ).let { movieRemoteMapper.mapToMovies(remoteMovies) }

    private suspend fun getMoviesFromLocal(keyword: String, searchType: SearchType) = try {
        movieLocalMapper.mapToMovies(
            movieLocalSource.getMoviesByKeywordAndSearchType(
                keyword = keyword, searchType = searchType
            )
        )
    } catch (_: Exception) {
        emptyList()
    }

    private suspend fun saveMoviesWithSearch(
        remoteMovies: RemoteMovieResponse, keyword: String, searchType: SearchType
    ) = movieLocalSource.addMoviesBySearchData(
        movies = movieRemoteMapper.mapToLocalMovies(remoteMovies),
        searchKeyword = keyword,
        searchType = searchType,
        expireDate = Clock.System.now()
    )
}