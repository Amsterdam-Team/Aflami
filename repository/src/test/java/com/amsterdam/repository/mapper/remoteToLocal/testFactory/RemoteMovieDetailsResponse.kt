package com.amsterdam.repository.mapper.remoteToLocal.testFactory

import com.amsterdam.repository.dto.remote.ProductionCompanyDto
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteCategoryDto
import com.amsterdam.repository.dto.remote.RemoteMovieDetailsResponse
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
import com.amsterdam.repository.dto.remote.VideoResponse
import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.dto.remote.review.ReviewsResponse

fun createRemoteMovieDetailsResponse(
    id: Long = 550L,
    title: String = "Fight Club",
    overview: String = "A ticking-time-bomb insomniac...",
    posterPath: String? = "/poster.jpg",
    releaseDate: String = "1999-10-15",
    voteAverage: Double = 8.4,
    popularity: Double = 50.0,
    runtime: Int = 139,
    originCountry: List<String> = listOf("US", "DE"),
    adult: Boolean = false,
    backdropPath: String? = "/backdrop.jpg",
    genreIds: List<Int> = emptyList(),
    originalLanguage: String = "en",
    originalTitle: String = "Fight Club",
    productionCompanies: List<ProductionCompanyDto> = emptyList(),
    video: Boolean = false,
    voteCount: Int = 1000,
    genres: List<RemoteCategoryDto> = emptyList(),
    reviews: ReviewsResponse = ReviewsResponse(
        page = 1,
        results = emptyList(),
        totalPages = 1,
        totalResults = 0
    ),
    credits: RemoteCastAndCrewResponse = RemoteCastAndCrewResponse(
        cast = emptyList(),
        crew = emptyList()
    ),
    similar: RemoteMovieResponse = RemoteMovieResponse(
        results = emptyList(),
        page = 1,
        totalPages = 1,
        totalResults = 0
    ),
    images: RemoteGalleryResponse = RemoteGalleryResponse(
        backdrops = emptyList(),
        posters = emptyList(),
        id = id,
        logos = emptyList()
    ),
    videos: VideoResponse = VideoResponse(results = emptyList())
): RemoteMovieDetailsResponse {
    return RemoteMovieDetailsResponse(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        popularity = popularity,
        runtime = runtime,
        originCountry = originCountry,
        adult = adult,
        backdropPath = backdropPath,
        genreIds = genreIds,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        productionCompanies = productionCompanies,
        video = video,
        voteCount = voteCount,
        genres = genres,
        reviews = reviews,
        credits = credits,
        similar = similar,
        images = images,
        videos = videos
    )
}