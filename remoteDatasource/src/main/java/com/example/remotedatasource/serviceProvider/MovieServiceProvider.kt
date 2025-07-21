package com.example.remotedatasource.serviceProvider

import com.example.repository.dto.remote.ProductionCompanyResponse
import com.example.repository.dto.remote.RemoteActorSearchResponse
import com.example.repository.dto.remote.RemoteCastAndCrewResponse
import com.example.repository.dto.remote.RemoteMovieItemDto
import com.example.repository.dto.remote.RemoteMovieResponse
import com.example.repository.dto.remote.movieGallery.RemoteMovieGalleryResponse
import com.example.repository.dto.remote.review.ReviewsResponse

interface MovieServiceProvider {
    suspend fun getPopularMovies(): RemoteMovieResponse
    suspend fun getMoviesByKeyword(keyword: String, page: Int): RemoteMovieResponse
    suspend fun getActorIdByName(name: String, page: Int): RemoteActorSearchResponse
    suspend fun getMoviesByActorId(actorIds: String): RemoteMovieResponse
    suspend fun getMoviesByCountryIsoCode(countryIsoCode: String, page: Int): RemoteMovieResponse
    suspend fun getCastByMovieId(movieId: Long): RemoteCastAndCrewResponse
    suspend fun getMovieReviews(movieId: Long): ReviewsResponse
    suspend fun getSimilarMovies(movieId: Long): RemoteMovieResponse
    suspend fun getMovieGallery(movieId: Long): RemoteMovieGalleryResponse
    suspend fun getMoviePosters(movieId: Long): RemoteMovieGalleryResponse
    suspend fun getProductionCompany(movieId: Long): ProductionCompanyResponse
    suspend fun getMovieDetailsById(movieId: Long): RemoteMovieItemDto
}