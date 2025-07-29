package com.amsterdam.domain.repository

import com.amsterdam.entity.Actor
import com.amsterdam.entity.Country
import com.amsterdam.entity.Movie
import com.amsterdam.entity.ProductionCompany
import com.amsterdam.entity.Review
import com.amsterdam.entity.category.MovieGenre

interface MovieRepository {
    suspend fun getMoviesByKeyword(keyword: String, page: Int, moviesPerPage: Int): List<Movie>
    suspend fun getMoviesByActor(actorName: String, page: Int, moviesPerPage: Int): List<Movie>
    suspend fun getMoviesByCountry(country: Country, page: Int, moviesPerPage: Int): List<Movie>

    suspend fun getActorsByMovieId(movieId: Long): List<Actor>
    suspend fun getMovieReviews(movieId : Long) : List<Review>
    suspend fun getMovieDetailsById(movieId : Long): Movie

    suspend fun getSimilarMovies(movieId : Long) : List<Movie>
    suspend fun getMovieGallery(movieId : Long) : List<String>
    suspend fun getMoviePosters(movieId : Long) : List<String>
    suspend fun getProductionCompany(movieId : Long) : List<ProductionCompany>
    suspend fun getPopularMovies(): List<Movie>
    suspend fun getTopRatedMovies(page: Int): List<Movie>
    suspend fun getUpcomingMovies(): List<Movie>
    suspend fun getMoviesByGenres(movieGenres: List<MovieGenre>): List<Movie>
}
