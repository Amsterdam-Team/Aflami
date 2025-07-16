package com.example.repository.repository

import com.example.domain.repository.MovieRepository
import com.example.entity.Movie
import com.example.repository.datasource.local.MovieLocalSource
import com.example.repository.datasource.remote.MovieRemoteSource
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.dto.remote.RemoteMovieResponse
import com.example.repository.mapper.local.MovieLocalMapper
import com.example.repository.mapper.remote.MovieRemoteMapper
import com.example.repository.utils.RecentSearchHandler
import com.example.repository.utils.tryToExecute
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Clock

class MovieRepositoryImpl(
    private val movieLocalSource: MovieLocalSource,
    private val movieDataSource: MovieRemoteSource,
    private val movieLocalMapper: MovieLocalMapper,
    private val movieRemoteMapper: MovieRemoteMapper,
    private val recentSearchHandler: RecentSearchHandler,
    private val movieRemoteMapper: RemoteMovieMapper,
    private val remoteCastMapper: RemoteCastMapper,
    private val recentSearchDatasource: LocalRecentSearchDataSource,
    private val remoteReviewMapper: RemoteReviewMapper,
    private val remoteGalleryMapper: RemoteGalleryMapper,
    private val remoteProductionCompanyMapper: RemoteProductionCompanyMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieRepository {
    override suspend fun getMoviesByKeyword(keyword: String): List<Movie> {
        var movies: List<Movie> = emptyList()
        val searchType = SearchType.BY_KEYWORD
        if (!recentSearchHandler.isExpired(keyword, searchType)) {
            movies = getMoviesFromLocal(keyword, searchType)
        }
        if (movies.isNotEmpty()) return movies
        recentSearchHandler.deleteRecentSearch(keyword, searchType)
        return getMoviesByKeywordFromRemote(keyword, searchType)
    }

    override suspend fun getMoviesByActor(actorName: String): List<Movie> {
        var movies: List<Movie> = emptyList()
        val searchType = SearchType.BY_ACTOR
        if (!recentSearchHandler.isExpired(keyword = actorName, searchType)) {
            movies = getMoviesFromLocal(keyword = actorName, searchType)
        }
        if (movies.isNotEmpty()) return movies
        recentSearchHandler.deleteRecentSearch(actorName, searchType)
        return getMoviesByActorNameFromRemote(actorName, searchType)
    }

    override suspend fun getMoviesByCountryIsoCode(countryIsoCode: String): List<Movie> {
        var movies: List<Movie> = emptyList()
        val searchType = SearchType.BY_KEYWORD
        if (!recentSearchHandler.isExpired(countryIsoCode, searchType)) {
            movies = getMoviesFromLocal(countryIsoCode, searchType)
        }
        if (movies.isNotEmpty()) return movies
        recentSearchHandler.deleteRecentSearch(countryIsoCode, searchType)
        return getMoviesByCountryIsoCodeFromRemote(countryIsoCode, searchType)
    }

    private suspend fun getMoviesByKeywordFromRemote(
        keyword: String,
        searchType: SearchType
    ): List<Movie> {
        return tryToExecute(
            function = {
                movieDataSource.getMoviesByKeyword(keyword)
            },
            onSuccess = { remoteMovies ->
                saveMoviesWithSearch(
                    remoteMovies,
                    keyword = keyword,
                    searchType = searchType,
                )
                movieRemoteMapper.mapToMovies(remoteMovies)
            },
            onFailure = { aflamiException -> throw aflamiException }
        )
    }


    private suspend fun getMoviesByActorNameFromRemote(
        actorName: String,
        searchType: SearchType
    ): List<Movie> {
        return tryToExecute(
            function = { movieDataSource.getMoviesByActorName(actorName) },
            onSuccess = { remoteMovies ->
                saveMoviesWithSearch(
                    remoteMovies = remoteMovies,
                    keyword = actorName,
                    searchType = searchType
                )
                movieRemoteMapper.mapToMovies(remoteMovies)
            },
            onFailure = { aflamiException -> throw aflamiException },
            dispatcher = dispatcher
        )
    }

    private suspend fun getMoviesByCountryIsoCodeFromRemote(
        countryIsoCode: String,
        searchType: SearchType
    ): List<Movie> {
        return tryToExecute(
            function = { movieDataSource.getMoviesByCountryIsoCode(countryIsoCode) },
            onSuccess = { remoteMovies ->
                saveMoviesWithSearch(
                    remoteMovies = remoteMovies,
                    keyword = countryIsoCode,
                    searchType = searchType
                )
                movieRemoteMapper.mapToMovies(remoteMovies)
            },
            onFailure = { aflamiException -> throw aflamiException },
            dispatcher = dispatcher
        )
    }

    private suspend fun getMoviesFromLocal(keyword: String, searchType: SearchType): List<Movie> {
        return tryToExecute(
            function = {
                movieLocalSource.getMoviesByKeywordAndSearchType(
                    keyword = keyword,
                    searchType = searchType
                )
            },
            onSuccess = { localMovies -> movieLocalMapper.mapToMovies(localMovies) },
            onFailure = { emptyList() },
        )
    }

    private suspend fun saveMoviesWithSearch(
        remoteMovies: RemoteMovieResponse,
        keyword: String,
        searchType: SearchType
    ) {
        val localMovies = movieRemoteMapper.mapToLocalMovies(remoteMovies)
        tryToExecute(
            function = {
                movieLocalSource.addMoviesBySearchData(
                    movies = localMovies,
                    searchKeyword = keyword,
                    searchType = searchType,
                    expireDate = Clock.System.now()
                )
            },
            onSuccess = {},
            onFailure = {}
        )
    }

    override suspend fun getActorsByMovieId(id: Long): List<Actor> {
        return remoteMovieDataSource.getCastByMovieId(id).cast.map { remoteCastMapper.mapToDomain(it) }
    }

    override suspend fun getMovieReviews(movieId: Long): List<Review> =
        remoteReviewMapper.mapResponseToDomain(remoteMovieDataSource.getMovieReviews(movieId))

    override suspend fun getMovieDetailsById(movieId: Long): Movie {
        return movieRemoteMapper.mapToDomain(remoteMovieDataSource.getMovieDetailsById(movieId))
    }

    override suspend fun getSimilarMovies(movieId: Long): List<Movie> =
        movieRemoteMapper.mapResponseToDomain(remoteMovieDataSource.getSimilarMovies(movieId))

    override suspend fun getMovieGallery(movieId: Long): List<String> =
        remoteGalleryMapper.mapGalleryToDomain(remoteMovieDataSource.getMovieGallery(movieId))

    override suspend fun getProductionCompany(movieId: Long): List<ProductionCompany> {
      return  remoteProductionCompanyMapper.mapProductionCompanyToDomain(
            remoteMovieDataSource.getProductionCompany(movieId)
        )
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