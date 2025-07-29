package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.ProductionCompanyResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.dto.remote.review.ReviewsResponse

interface MovieRemoteSource {

    suspend fun getMoviesByKeyword(keyword: String, page:Int): RemoteMovieResponse

    suspend fun getMoviesByActorName(name: String, page:Int): RemoteMovieResponse

    suspend fun getMoviesByCountryIsoCode(countryIsoCode: String, page:Int): RemoteMovieResponse

    suspend fun getCastByMovieId(movieId: Long): RemoteCastAndCrewResponse

    suspend fun getMovieReviews(movieId: Long): ReviewsResponse

    suspend fun getSimilarMovies(movieId: Long): RemoteMovieResponse

    suspend fun getMovieGallery(movieId: Long): RemoteGalleryResponse

    suspend fun getProductionCompany(movieId: Long): ProductionCompanyResponse

    suspend fun getMovieDetailsById(movieId: Long): RemoteMovieItemDto

    suspend fun getMoviePosters(movieId : Long) : RemoteGalleryResponse

    suspend fun getPopularMovies(): RemoteMovieResponse

    suspend fun getUpcomingMovies(): RemoteMovieResponse

    suspend fun getTopRatedMovies(page: Int): RemoteMovieResponse

    suspend fun getMoviesByGenreIds(genresIds: List<Long>): RemoteMovieResponse
}