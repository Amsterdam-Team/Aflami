package com.amsterdam.repository.mapper.remoteToLocal.testFactory

import com.amsterdam.repository.dto.remote.ProductionCompanyRemoteDto
import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.CategoryRemoteDto
import com.amsterdam.repository.dto.remote.MovieDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.MovieRemoteResponse
import com.amsterdam.repository.dto.remote.VideoRemoteResponse
import com.amsterdam.repository.dto.remote.movieGallery.GalleryRemoteResponse
import com.amsterdam.repository.dto.remote.review.ReviewsRemoteResponse

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
    productionCompanies: List<ProductionCompanyRemoteDto> = emptyList(),
    video: Boolean = false,
    voteCount: Int = 1000,
    genres: List<CategoryRemoteDto> = emptyList(),
    reviews: ReviewsRemoteResponse = ReviewsRemoteResponse(
        page = 1,
        results = emptyList(),
        totalPages = 1,
        totalResults = 0
    ),
    credits: CastAndCrewRemoteResponse = CastAndCrewRemoteResponse(
        cast = emptyList(),
        crew = emptyList()
    ),
    similar: MovieRemoteResponse = MovieRemoteResponse(
        results = emptyList(),
        page = 1,
        totalPages = 1,
        totalResults = 0
    ),
    images: GalleryRemoteResponse = GalleryRemoteResponse(
        backdrops = emptyList(),
        posters = emptyList(),
        id = id,
        logos = emptyList()
    ),
    videos: VideoRemoteResponse = VideoRemoteResponse(results = emptyList())
): MovieDetailsRemoteResponse {
    return MovieDetailsRemoteResponse(
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