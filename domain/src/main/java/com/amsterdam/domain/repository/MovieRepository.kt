package com.amsterdam.domain.repository

import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase.MovieDetails
import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase.UserRatedMovie
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Country
import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre

interface MovieRepository {
    suspend fun getMoviesByKeyword(keyword: String, page: Int, moviesPerPage: Int): List<Movie>
    suspend fun getMoviesByActor(actorName: String, page: Int, moviesPerPage: Int): List<Movie>
    suspend fun getMoviesByCountry(country: Country, page: Int, moviesPerPage: Int): List<Movie>

    suspend fun getActorsByMovieId(movieId: Long): List<Actor>
    suspend fun getMovieDetailsById(movieId : Long): MovieDetails

    suspend fun getPopularMovies(): List<Movie>
    suspend fun getTopRatedMovies(page: Int): List<Movie>
    suspend fun getUpcomingMovies(): List<Movie>
    suspend fun getMoviesByGenres(movieGenres: List<MovieGenre>, page: Int): List<Movie>

    suspend fun setMovieRate(rate: Int, movieId: Long)
    suspend fun getUserRatedMovies(): List<UserRatedMovie>
    suspend fun deleteMovieRate(movieId: Long)
}
