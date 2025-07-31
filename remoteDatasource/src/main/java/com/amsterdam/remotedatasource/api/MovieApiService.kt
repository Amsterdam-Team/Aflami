package com.amsterdam.remotedatasource.api

import com.amsterdam.repository.dto.remote.RemoteActorSearchResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteMovieDetailsResponse
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET(MOVIE_POPULAR)
    suspend fun getPopularMovies(): RemoteMovieResponse

    @GET(MOVIE_UPCOMING)
    suspend fun getUpcomingMovies(): RemoteMovieResponse

    @GET(SEARCH_MOVIE_URL)
    suspend fun getMoviesByKeyword(
        @Query(QUERY_KEY) keyword: String,
        @Query(PAGE_KEY) page: Int
    ): RemoteMovieResponse

    @GET(SEARCH_PERSON_URL)
    suspend fun getActorIdByName(
        @Query(QUERY_KEY) name: String,
        @Query(PAGE_KEY) page: Int
    ): RemoteActorSearchResponse

    @GET(DISCOVER_MOVIE)
    suspend fun getMoviesByActorId(
        @Query(WITH_CAST_KEY) actorIds: String
    ): RemoteMovieResponse

    @GET(DISCOVER_MOVIE)
    suspend fun getMoviesByCountryIsoCode(
        @Query(WITH_ORIGIN_COUNTRY_KEY) countryIsoCode: String,
        @Query(PAGE_KEY) page: Int
    ): RemoteMovieResponse

    @GET(MOVIE_CREDITS_ENDPOINT)
    suspend fun getCastByMovieId(
        @Path("movieId") movieId: Long
    ): RemoteCastAndCrewResponse

    @GET(MOVIE_DETAILS_ENDPOINT)
    suspend fun getMovieDetailsById(
        @Path("movieId") movieId: Long,
        @Query("append_to_response") appendToResponse: String = MOVIE_DETAILS_APPEND_PARAMETERS
    ): RemoteMovieDetailsResponse

    @GET(DISCOVER_MOVIE)
    suspend fun getMoviesByGenreIds(
        @Query(GENRES) genresIds: List<Long>
    ): RemoteMovieResponse

    @GET(TOP_RATED_MOVIES)
    suspend fun getTopRatedMovies() : RemoteMovieResponse

    companion object {
        private const val MOVIE_POPULAR = "movie/popular"

        private const val MOVIE_UPCOMING = "movie/upcoming"

        private const val SEARCH_MOVIE_URL = "search/movie"
        private const val SEARCH_PERSON_URL = "search/person"
        private const val DISCOVER_MOVIE = "discover/movie"

        const val GENRES = "with_genres"

        const val MOVIE_DETAILS_APPEND_PARAMETERS = "reviews,credits,actors,similar,images,videos"
        private const val MOVIE_CREDITS_ENDPOINT = "movie/{movieId}/credits"
        private const val MOVIE_DETAILS_ENDPOINT = "movie/{movieId}"

        private const val QUERY_KEY = "query"
        private const val PAGE_KEY = "page"
        private const val WITH_CAST_KEY = "with_cast"
        private const val WITH_ORIGIN_COUNTRY_KEY = "with_origin_country"
        private const val TOP_RATED_MOVIES = "movie/top_rated"
    }
}