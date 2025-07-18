package com.example.domain.repository

import com.example.entity.Actor
import com.example.entity.Country
import com.example.entity.Movie
import com.example.entity.ProductionCompany
import com.example.entity.Review

interface MovieRepository {
    suspend fun getMoviesByKeyword(keyword: String): List<Movie>
    suspend fun getMoviesByActor(actorName: String): List<Movie>
    suspend fun getMoviesByCountry(country: Country): List<Movie>

    suspend fun getActorsByMovieId(movieId: Long): List<Actor>
    suspend fun getMovieReviews(movieId : Long) : List<Review>
    suspend fun getMovieDetailsById(movieId : Long): Movie

    suspend fun getSimilarMovies(movieId : Long) : List<Movie>
    suspend fun getMovieGallery(movieId : Long) : List<String>
    suspend fun getMoviePosters(movieId : Long) : List<String>
    suspend fun getProductionCompany(movieId : Long) : List<ProductionCompany>
}
