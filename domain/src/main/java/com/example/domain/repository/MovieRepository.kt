package com.example.domain.repository

import com.example.domain.useCase.genreTypes.MovieGenre
import com.example.entity.Movie
import com.example.entity.Review

interface MovieRepository {
    suspend fun getMoviesByKeyword(keyword: String, rating: Float = 0f, movieGenre: MovieGenre = MovieGenre.ALL): List<Movie>
    suspend fun getMoviesByActor(actorName: String): List<Movie>
    suspend fun getMoviesByCountryIsoCode(countryIsoCode: String): List<Movie>
    suspend fun getMovieReviews(movieId : Long) : List<Review>
    suspend fun getMovieById(movieId : Long): Movie
}
