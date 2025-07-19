package com.example.repository.datasource.remote

import com.example.repository.dto.remote.ProductionCompanyResponse
import com.example.repository.dto.remote.RemoteCastAndCrewResponse
import com.example.repository.dto.remote.RemoteMovieItemDto
import com.example.repository.dto.remote.RemoteMovieResponse
import com.example.repository.dto.remote.movieGallery.RemoteMovieGalleryResponse
import com.example.repository.dto.remote.review.ReviewsResponse

interface MovieRemoteSource {

    suspend fun getMoviesByKeyword(keyword: String): RemoteMovieResponse

    suspend fun getMoviesByActorName(name: String): RemoteMovieResponse

    suspend fun getMoviesByCountryIsoCode(countryIsoCode: String): RemoteMovieResponse

    suspend fun getCastByMovieId(movieId: Long): RemoteCastAndCrewResponse

    suspend fun getMovieReviews(movieId: Long): ReviewsResponse

    suspend fun getSimilarMovies(movieId: Long): RemoteMovieResponse

    suspend fun getMovieGallery(movieId: Long): RemoteMovieGalleryResponse

    suspend fun getProductionCompany(movieId: Long): ProductionCompanyResponse

    suspend fun getMovieDetailsById(movieId: Long): RemoteMovieItemDto

    suspend fun getMoviePosters(movieId : Long) : RemoteMovieGalleryResponse
}