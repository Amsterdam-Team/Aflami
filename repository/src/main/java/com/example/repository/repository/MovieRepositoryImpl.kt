package com.example.repository.repository

import com.example.domain.repository.MovieRepository
import com.example.entity.Actor
import com.example.entity.Country
import com.example.entity.Movie
import com.example.entity.ProductionCompany
import com.example.entity.Review
import com.example.repository.datasource.local.MovieLocalSource
import com.example.repository.datasource.remote.MovieRemoteSource
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.dto.remote.RemoteMovieResponse
import com.example.repository.mapper.local.MovieWithCategoriesLocalMapper
import com.example.repository.mapper.remote.CastRemoteMapper
import com.example.repository.mapper.remote.GalleryRemoteMapper
import com.example.repository.mapper.remote.MovieRemoteMapper
import com.example.repository.mapper.remote.PostersRemoteMapper
import com.example.repository.mapper.remote.ProductionCompanyRemoteMapper
import com.example.repository.mapper.remote.ReviewRemoteMapper
import com.example.repository.mapper.remoteToLocal.MovieRemoteLocalMapper
import com.example.repository.utils.RecentSearchHandler
import com.example.repository.utils.tryToExecute
import kotlinx.datetime.Clock

class MovieRepositoryImpl(
    private val movieLocalSource: MovieLocalSource,
    private val movieRemoteDataSource: MovieRemoteSource,
    private val movieRemoteMapper: MovieRemoteMapper,
    private val recentSearchHandler: RecentSearchHandler,
    private val castRemoteMapper: CastRemoteMapper,
    private val reviewRemoteMapper: ReviewRemoteMapper,
    private val galleryRemoteMapper: GalleryRemoteMapper,
    private val posterRemoteMapper: PostersRemoteMapper,
    private val remoteProductionCompanyMapper: ProductionCompanyRemoteMapper,
    private val movieWithCategoriesLocalMapper: MovieWithCategoriesLocalMapper,
    private val movieRemoteLocalMapper: MovieRemoteLocalMapper,
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

    override suspend fun getMoviesByCountry(country: Country): List<Movie> {
        var movies: List<Movie> = emptyList()
        val searchType = SearchType.BY_COUNTRY
        if (!recentSearchHandler.isExpired(country.countryIsoCode, searchType)) {
            movies = getMoviesFromLocal(country.countryIsoCode, searchType)
        }
        if (movies.isNotEmpty()) return movies
        recentSearchHandler.deleteRecentSearch(country.countryIsoCode, searchType)
        return getMoviesByCountryIsoCodeFromRemote(country.countryIsoCode, searchType)
    }

    private suspend fun getMoviesByKeywordFromRemote(
        keyword: String,
        searchType: SearchType
    ): List<Movie> {
        return tryToExecute(
            function = {
                movieRemoteDataSource.getMoviesByKeyword(keyword)
            },
            onSuccess = { remoteMovies ->
                saveMoviesWithSearch(
                    remoteMovies,
                    keyword = keyword,
                    searchType = searchType,
                )
                movieRemoteMapper.toEntityList(remoteMovies.results)
            },
            onFailure = { aflamiException -> throw aflamiException }
        )
    }


    private suspend fun getMoviesByActorNameFromRemote(
        actorName: String,
        searchType: SearchType
    ): List<Movie> {
        return tryToExecute(
            function = { movieRemoteDataSource.getMoviesByActorName(actorName) },
            onSuccess = { remoteMovies ->
                saveMoviesWithSearch(
                    remoteMovies = remoteMovies,
                    keyword = actorName,
                    searchType = searchType
                )
                movieRemoteMapper.toEntityList(remoteMovies.results)
            },
            onFailure = { aflamiException -> throw aflamiException },
        )
    }

    private suspend fun getMoviesByCountryIsoCodeFromRemote(
        countryIsoCode: String,
        searchType: SearchType
    ): List<Movie> {
        return tryToExecute(
            function = { movieRemoteDataSource.getMoviesByCountryIsoCode(countryIsoCode) },
            onSuccess = { remoteMovies ->
                saveMoviesWithSearch(
                    remoteMovies = remoteMovies,
                    keyword = countryIsoCode,
                    searchType = searchType
                )
                movieRemoteMapper.toEntityList(remoteMovies.results)
            },
            onFailure = { aflamiException -> throw aflamiException },
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
            onSuccess = { localMovies -> movieWithCategoriesLocalMapper.toEntityList(localMovies) },
            onFailure = { emptyList() },
        )
    }

    private suspend fun saveMoviesWithSearch(
        remoteMovies: RemoteMovieResponse,
        keyword: String,
        searchType: SearchType
    ) {
        val localMovies = movieRemoteLocalMapper.toLocalList(remoteMovies.results)
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

    override suspend fun getActorsByMovieId(movieId: Long): List<Actor> =
        movieRemoteDataSource.getCastByMovieId(movieId).cast.map { castRemoteMapper.toEntity(it) }

    override suspend fun getMovieReviews(movieId: Long): List<Review> =
        reviewRemoteMapper.toEntityList(movieRemoteDataSource.getMovieReviews(movieId).results)

    override suspend fun getMovieDetailsById(movieId: Long): Movie {
        return movieRemoteMapper.toEntity(movieRemoteDataSource.getMovieDetailsById(movieId))
    }

    override suspend fun getSimilarMovies(movieId: Long): List<Movie> =
        movieRemoteMapper.toEntityList(movieRemoteDataSource.getSimilarMovies(movieId).results)

    override suspend fun getMovieGallery(movieId: Long): List<String> =
        galleryRemoteMapper.toEntity(movieRemoteDataSource.getMovieGallery(movieId))

    override suspend fun getMoviePosters(movieId: Long): List<String> =
        posterRemoteMapper.mapPostersToDomain(movieRemoteDataSource.getMoviePosters(movieId))


    override suspend fun getProductionCompany(movieId: Long): List<ProductionCompany> {
        return remoteProductionCompanyMapper.toEntityList(
            movieRemoteDataSource.getProductionCompany(movieId).productionCompanies
        )
    }

}