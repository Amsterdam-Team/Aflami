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
import com.example.repository.mapper.remote.ProductionCompanyRemoteMapper
import com.example.repository.mapper.remote.ReviewRemoteMapper
import com.example.repository.mapper.remoteToLocal.MovieRemoteLocalMapper
import com.example.repository.utils.RecentSearchHandler
import kotlinx.datetime.Clock

class MovieRepositoryImpl(
    private val movieLocalSource: MovieLocalSource,
    private val movieRemoteDataSource: MovieRemoteSource,
    private val movieRemoteMapper: MovieRemoteMapper,
    private val recentSearchHandler: RecentSearchHandler,
    private val castRemoteMapper: CastRemoteMapper,
    private val reviewRemoteMapper: ReviewRemoteMapper,
    private val galleryRemoteMapper: GalleryRemoteMapper,
    private val remoteProductionCompanyMapper: ProductionCompanyRemoteMapper,
    private val movieWithCategoriesLocalMapper: MovieWithCategoriesLocalMapper,
    private val movieRemoteLocalMapper: MovieRemoteLocalMapper,
) : MovieRepository {
    override suspend fun getMoviesByKeyword(keyword: String): List<Movie> {
        return getCachedMovies(keyword, SearchType.BY_KEYWORD)
            ?: recentSearchHandler.deleteRecentSearch(
                keyword, SearchType.BY_KEYWORD
            ).let { getMoviesByKeywordFromRemote(keyword, SearchType.BY_KEYWORD) }
    }

    override suspend fun getMoviesByActor(actorName: String): List<Movie> {
        return getCachedMovies(actorName, SearchType.BY_ACTOR)
            ?: recentSearchHandler.deleteRecentSearch(
                actorName, SearchType.BY_ACTOR
            ).let { getMoviesByActorNameFromRemote(actorName, SearchType.BY_ACTOR) }
    }

    override suspend fun getMoviesByCountry(country: Country): List<Movie> {
        return getCachedMovies(country.countryIsoCode, SearchType.BY_COUNTRY)
            ?: recentSearchHandler.deleteRecentSearch(country.countryIsoCode, SearchType.BY_COUNTRY)
                .let {
                    getMoviesByCountryIsoCodeFromRemote(
                        country.countryIsoCode,
                        SearchType.BY_COUNTRY
                    )
                }
    }

    override suspend fun getActorsByMovieId(movieId: Long): List<Actor> {
        return castRemoteMapper.toEntityList(movieRemoteDataSource.getCastByMovieId(movieId).cast)
    }

    override suspend fun getMovieDetailsById(movieId: Long): Movie {
        return movieRemoteMapper.toEntity(movieRemoteDataSource.getMovieDetailsById(movieId))
    }

    override suspend fun getMovieReviews(movieId: Long): List<Review> {
        return reviewRemoteMapper.toEntityList(movieRemoteDataSource.getMovieReviews(movieId).results)
    }

    override suspend fun getSimilarMovies(movieId: Long): List<Movie> {
        return movieRemoteMapper.toEntityList(movieRemoteDataSource.getSimilarMovies(movieId).results)
    }

    override suspend fun getMovieGallery(movieId: Long): List<String> {
        return galleryRemoteMapper.toEntity(movieRemoteDataSource.getMovieGallery(movieId))
    }

    override suspend fun getProductionCompany(movieId: Long): List<ProductionCompany> {
        return remoteProductionCompanyMapper.toEntityList(
            movieRemoteDataSource.getProductionCompany(movieId).productionCompanies
        )
    }

    private suspend fun getCachedMovies(keyword: String, searchType: SearchType): List<Movie>? {
        return recentSearchHandler.isRecentSearchExpired(keyword, searchType)
            .takeIf { isRecentSearchExpired -> !isRecentSearchExpired }
            ?.let { getMoviesFromLocal(keyword, searchType) }
            ?.takeIf { movies -> movies.isNotEmpty() }
    }

    private suspend fun getMoviesByKeywordFromRemote(
        keyword: String, searchType: SearchType
    ): List<Movie> {
        return onSuccessGetMovies(
            movieRemoteDataSource.getMoviesByKeyword(keyword), keyword, searchType
        )
    }

    private suspend fun getMoviesByActorNameFromRemote(
        actorName: String, searchType: SearchType
    ): List<Movie> {
        return onSuccessGetMovies(
            movieRemoteDataSource.getMoviesByActorName(actorName), actorName, searchType
        )
    }

    private suspend fun getMoviesByCountryIsoCodeFromRemote(
        countryIsoCode: String, searchType: SearchType
    ): List<Movie> {
        return onSuccessGetMovies(
            movieRemoteDataSource.getMoviesByCountryIsoCode(countryIsoCode),
            countryIsoCode,
            searchType
        )
    }

    private suspend fun onSuccessGetMovies(
        remoteMovies: RemoteMovieResponse, actorName: String, searchType: SearchType
    ): List<Movie> {
        return saveMoviesWithSearch(
            remoteMovies, actorName, searchType
        ).let { movieRemoteMapper.toEntityList(remoteMovies.results) }
    }

    private suspend fun getMoviesFromLocal(keyword: String, searchType: SearchType): List<Movie> {
        return try {
            movieWithCategoriesLocalMapper.toEntityList(
                movieLocalSource.getMoviesByKeywordAndSearchType(
                    keyword = keyword, searchType = searchType
                )
            )
        } catch (_: Exception) {
            emptyList()
        }
    }

    private suspend fun saveMoviesWithSearch(
        remoteMovies: RemoteMovieResponse, keyword: String, searchType: SearchType
    ) {
        movieLocalSource.addMoviesBySearchData(
            movies = movieRemoteLocalMapper.toLocalList(remoteMovies.results),
            searchKeyword = keyword,
            searchType = searchType,
            expireDate = Clock.System.now()
        )
    }
}