package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.MovieApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.MovieRemoteDataSource
import com.amsterdam.repository.dto.remote.RatingRemoteResponse
import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.MovieDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.MovieItemRemoteDto
import com.amsterdam.repository.dto.remote.MovieRemoteResponse
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val movieApiService: MovieApiService,
) : MovieRemoteDataSource {

    override suspend fun getMoviesByKeyword(keyword: String, page: Int): MovieRemoteResponse {
        return responseCall { movieApiService.getMoviesByKeyword(keyword, page) }
    }

    override suspend fun getMoviesByActorIds(actorIds: List<Int>, page: Int): MovieRemoteResponse {
        val actorIdsAsString = actorIds.joinToString(separator = "|")
        return responseCall { movieApiService.getMoviesByActorId(actorIdsAsString) }
    }

    override suspend fun getActorIdsByName(name: String, page: Int): List<Int> {
        return responseCall { movieApiService.getActorIdByName(name, page) }
            .actors
            .map { it.id }
    }

    override suspend fun getMoviesByCountryIsoCode(
        countryIsoCode: String,
        page: Int
    ): MovieRemoteResponse {
        return responseCall { movieApiService.getMoviesByCountryIsoCode(countryIsoCode, page) }
    }

    override suspend fun getCastByMovieId(movieId: Long): CastAndCrewRemoteResponse {
        return responseCall { movieApiService.getCastByMovieId(movieId) }
    }

    override suspend fun getMovieDetailsById(movieId: Long): MovieDetailsRemoteResponse {
        return responseCall { movieApiService.getMovieDetailsById(movieId) }
    }

    override suspend fun getPopularMovies(page: Int): MovieRemoteResponse {
        return responseCall { movieApiService.getPopularMovies(page) }
    }

    override suspend fun getUpcomingMovies(): MovieRemoteResponse {
        return responseCall { movieApiService.getUpcomingMovies() }
    }

    override suspend fun getTopRatedMovies(page: Int): MovieRemoteResponse {
        return responseCall { movieApiService.getTopRatedMovies(page) }
    }

    override suspend fun getMoviesByGenreIds(
        genresIds: List<Long>,
        page: Int
    ): MovieRemoteResponse {
        return responseCall { movieApiService.getMoviesByGenreIds(genresIds, page) }
    }

    override suspend fun getMoviesByGenreId(
        genreId: Long,
        page: Int
    ): MovieRemoteResponse {
        return responseCall { movieApiService.getMoviesByGenreIds(listOf(genreId), page) }
    }

    override suspend fun setMovieRate(rate: Float, movieId: Long): RatingRemoteResponse? {
        return responseCall { movieApiService.postMovieRating(movieId = movieId, rate = rate) }
    }

    override suspend fun getRatedMovies(): MovieRemoteResponse {
        return responseCall { movieApiService.getRatedMovies() }
    }

    override suspend fun deleteMovieRate(movieId: Long) {
        responseCall { movieApiService.deleteMovieRate(movieId = movieId) }
    }

    override suspend fun getRandomMoviesWithNotNullDate(requiredMoviesNumber: Int): List<MovieItemRemoteDto> {
        val totalPages = 500
        val collectedMovies = mutableListOf<MovieItemRemoteDto>()
        val usedPages = mutableSetOf<Int>()

        while (collectedMovies.size < requiredMoviesNumber && usedPages.size < totalPages) {
            val randomPage = (1..totalPages).random().also { usedPages.add(it) }
            val pageMovies = getPopularMoviesByPage(randomPage)
                .filter { it.releaseDate != null }

            for (movie in pageMovies) {
                if (!collectedMovies.contains(movie)) {
                    collectedMovies.add(movie)
                    if (collectedMovies.size == requiredMoviesNumber) break
                }
            }
        }

        return collectedMovies
    }

    override suspend fun getRandomMoviesWithNotNullPoster(requiredMoviesNumber: Int): List<MovieItemRemoteDto> {
        val totalPages = 500
        val collectedMovies = mutableListOf<MovieItemRemoteDto>()
        val usedPages = mutableSetOf<Int>()

        while (collectedMovies.size < requiredMoviesNumber && usedPages.size < totalPages) {
            val randomPage = (1..totalPages).random().also { usedPages.add(it) }
            val pageMovies = getPopularMoviesByPage(randomPage)
                .filter { it.posterPath != null }

            for (movie in pageMovies) {
                if (!collectedMovies.contains(movie)) {
                    collectedMovies.add(movie)
                    if (collectedMovies.size == requiredMoviesNumber) break
                }
            }
        }
        return collectedMovies
    }


    private suspend fun getPopularMoviesByPage(page: Int): List<MovieItemRemoteDto> {
        return getPopularMovies(page = page).results
    }

}