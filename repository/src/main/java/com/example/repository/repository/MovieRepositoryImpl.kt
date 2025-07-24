package com.example.repository.repository

import com.example.domain.repository.CategoryRepository
import com.example.domain.repository.MovieRepository
import com.example.entity.Actor
import com.example.entity.Country
import com.example.entity.Movie
import com.example.entity.ProductionCompany
import com.example.entity.Review
import com.example.repository.datasource.local.MovieLocalSource
import com.example.repository.datasource.remote.MovieRemoteSource
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.dto.remote.RemoteCategoryDto
import com.example.repository.dto.remote.RemoteMovieItemDto
import com.example.repository.dto.remote.RemoteMovieResponse
import com.example.repository.mapper.local.MovieWithCategoriesLocalMapper
import com.example.repository.mapper.remote.CastRemoteMapper
import com.example.repository.mapper.remote.GalleryRemoteMapper
import com.example.repository.mapper.remote.MovieRemoteMapper
import com.example.repository.mapper.remote.PostersRemoteMapper
import com.example.repository.mapper.remote.ProductionCompanyRemoteMapper
import com.example.repository.mapper.remote.ReviewRemoteMapper
import com.example.repository.mapper.remoteToLocal.MovieGenreIdsRemoteLocalMapper
import com.example.repository.mapper.remoteToLocal.MovieRemoteLocalMapper
import com.example.repository.utils.RecentSearchHandler
import com.example.repository.utils.getDeviceLanguage

class MovieRepositoryImpl(
    private val categoryRepository: CategoryRepository,
    private val movieLocalSource: MovieLocalSource,
    private val movieRemoteDataSource: MovieRemoteSource,
    private val movieGenreIdsRemoteLocalMapper: MovieGenreIdsRemoteLocalMapper,
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
    override suspend fun getMoviesByKeyword(
        keyword: String,
        page: Int,
        moviesPerPage: Int
    ): List<Movie> {
        categoryRepository.getMovieCategories()
        return getCachedMovies(keyword, SearchType.BY_KEYWORD, page, moviesPerPage)
            ?: recentSearchHandler.deleteRecentSearch(
                keyword, SearchType.BY_KEYWORD, getDeviceLanguage()
            ).let {
                getMoviesByKeywordFromRemote(
                    keyword,
                    SearchType.BY_KEYWORD,
                    page,
                    moviesPerPage
                )
            }
    }

    override suspend fun getMoviesByActor(
        actorName: String,
        page: Int,
        moviesPerPage: Int
    ): List<Movie> {
        return getCachedMovies(actorName, SearchType.BY_ACTOR, page, moviesPerPage)
            ?: recentSearchHandler.deleteRecentSearch(
                actorName, SearchType.BY_ACTOR, getDeviceLanguage()
            ).let {
                getMoviesByActorNameFromRemote(
                    actorName,
                    SearchType.BY_ACTOR,
                    page,
                    moviesPerPage
                )
            }
    }

    override suspend fun getMoviesByCountry(
        country: Country,
        page: Int,
        moviesPerPage: Int
    ): List<Movie> {
        return getCachedMovies(country.countryIsoCode, SearchType.BY_COUNTRY, page, moviesPerPage)
            ?: recentSearchHandler.deleteRecentSearch(
                country.countryIsoCode,
                SearchType.BY_COUNTRY,
                getDeviceLanguage()
            )
                .let {
                    getMoviesByCountryIsoCodeFromRemote(
                        country.countryIsoCode,
                        SearchType.BY_COUNTRY,
                        page,
                        moviesPerPage
                    )
                }
    }

    override suspend fun getActorsByMovieId(movieId: Long): List<Actor> {
        return castRemoteMapper.toEntityList(movieRemoteDataSource.getCastByMovieId(movieId).cast)
    }

    override suspend fun getMovieDetailsById(movieId: Long): Movie {
        return movieRemoteMapper.toEntity(
            movieRemoteDataSource.getMovieDetailsById(movieId)
                .also { incrementUserInterestByMovie(it.genres)
                    cacheWatchedMovie(it)
                }
        )
    }

    private suspend fun cacheWatchedMovie(remoteMovieItemDto : RemoteMovieItemDto) {
        movieLocalSource.insertMovie(
            movieRemoteLocalMapper.toLocal(
                remote = remoteMovieItemDto, args = listOf(getDeviceLanguage())
            )
        )
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

    override suspend fun getMoviePosters(movieId: Long): List<String> =
        posterRemoteMapper.toEntity(movieRemoteDataSource.getMoviePosters(movieId))

    override suspend fun getProductionCompany(movieId: Long): List<ProductionCompany> {
        return remoteProductionCompanyMapper.toEntityList(
            movieRemoteDataSource.getProductionCompany(movieId).productionCompanies
        )
    }

    override suspend fun getUpcomingMovies(): List<Movie> {
        return movieRemoteMapper.toEntityList(movieRemoteDataSource.getUpcomingMovies().results)
    }

    override suspend fun getPopularMovies(): List<Movie> =
        movieRemoteMapper.toEntityList(movieRemoteDataSource.getPopularMovies().results)

    override suspend fun getTopRatedMovies(): List<Movie> =
        movieRemoteMapper.toEntityList(movieRemoteDataSource.getTopRatedMovies().results)

    private suspend fun getCachedMovies(
        keyword: String,
        searchType: SearchType,
        page: Int,
        moviesPerPage: Int
    ): List<Movie>? {
        return recentSearchHandler.isRecentSearchExpired(keyword, searchType, getDeviceLanguage())
            .takeIf { isRecentSearchExpired -> !isRecentSearchExpired }
            ?.let { getMoviesFromLocal(keyword, searchType, page, moviesPerPage) }
            ?.takeIf { movies -> movies.isNotEmpty() }
    }

    private suspend fun getMoviesByKeywordFromRemote(
        keyword: String, searchType: SearchType, page: Int, moviesPerPage: Int
    ): List<Movie> {
        return onSuccessGetRemoteMovies(
            movieRemoteDataSource.getMoviesByKeyword(keyword, page),
            keyword,
            searchType,
            page,
            moviesPerPage
        )
    }

    private suspend fun getMoviesByActorNameFromRemote(
        actorName: String, searchType: SearchType, page: Int, moviesPerPage: Int
    ): List<Movie> {
        return onSuccessGetRemoteMovies(
            movieRemoteDataSource.getMoviesByActorName(actorName, page),
            actorName,
            searchType,
            page,
            moviesPerPage
        )
    }

    private suspend fun getMoviesByCountryIsoCodeFromRemote(
        countryIsoCode: String, searchType: SearchType, page: Int, moviesPerPage: Int
    ): List<Movie> {
        return onSuccessGetRemoteMovies(
            movieRemoteDataSource.getMoviesByCountryIsoCode(countryIsoCode, page),
            countryIsoCode,
            searchType,
            page,
            moviesPerPage
        )
    }

    private suspend fun onSuccessGetRemoteMovies(
        remoteMovies: RemoteMovieResponse,
        keyword: String,
        searchType: SearchType,
        page: Int,
        moviesPerPage: Int
    ): List<Movie> {
        saveMovieWithCategories(remoteMovies)
        return saveMoviesWithSearch(remoteMovies, keyword, searchType)
            .let { getMoviesFromLocal(keyword, searchType, page, moviesPerPage) }
            .takeIf { movies -> movies.isNotEmpty() }
            ?: movieRemoteMapper.toEntityList(remoteMovies.results)
    }

    private suspend fun getMoviesFromLocal(
        keyword: String,
        searchType: SearchType,
        page: Int,
        moviesPerPage: Int
    ): List<Movie> {
        return try {
            movieWithCategoriesLocalMapper.toEntityList(
                movieLocalSource.getMoviesByKeywordAndSearchType(
                    keyword = keyword,
                    searchType = searchType,
                    storedLanguage = getDeviceLanguage(),
                    limit = moviesPerPage,
                    offset = moviesPerPage * (page - 1)
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
            movies = movieRemoteLocalMapper.toLocalList(
                remoteMovies.results,
                listOf(getDeviceLanguage())
            ),
            searchKeyword = keyword,
            searchType = searchType
        )
    }


    private suspend fun saveMovieWithCategories(remoteMovies: RemoteMovieResponse) {
        remoteMovies.results.forEach { onSaveMovieWithCategories(it) }
    }

    private suspend fun onSaveMovieWithCategories(remoteMovie: RemoteMovieItemDto) {
        movieLocalSource.addMovieWithCategories(
            movie = movieRemoteLocalMapper.toLocal(remoteMovie, listOf(getDeviceLanguage())),
            categories = movieGenreIdsRemoteLocalMapper.toLocalList(
                remoteMovie.genreIds,
                listOf(getDeviceLanguage())
            ),
            storedLanguage = getDeviceLanguage()
        )
    }

    private suspend fun incrementUserInterestByMovie(remoteCategories: List<RemoteCategoryDto>) {
        remoteCategories.map(RemoteCategoryDto::id)
            .map { movieLocalSource.incrementGenreInterest(it.toLong()) }
    }

}