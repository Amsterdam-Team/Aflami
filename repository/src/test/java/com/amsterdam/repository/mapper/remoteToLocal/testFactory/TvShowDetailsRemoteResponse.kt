package com.amsterdam.repository.mapper.remoteToLocal.testFactory

import com.amsterdam.repository.dto.remote.ProductionCompanyDto
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteCategoryDto
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.SeasonDto
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.VideoResponse
import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.dto.remote.review.ReviewsResponse

fun createTvShowDetailsRemoteResponse(
    id: Long = 1399L,
    title: String = "Game of Thrones",
    overview: String = "Seven noble families fight for control of the mythical land of Westeros.",
    posterPath: String? = "/poster.jpg",
    releaseDate: String = "2011-04-17",
    voteAverage: Double = 8.4,
    popularity: Double = 300.0,
    seasonCount: Int = 8,
    originCountry: List<String> = listOf("US"),
    adult: Boolean = false,
    backdropPath: String? = "/backdrop.jpg",
    genres: List<RemoteCategoryDto> = emptyList(),
    originalLanguage: String = "en",
    originalTitle: String = "Game of Thrones",
    seasons: List<SeasonDto> = emptyList(),
    productionCompanies: List<ProductionCompanyDto> = emptyList(),
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
    similar: RemoteTvShowResponse = RemoteTvShowResponse(
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
): TvShowDetailsRemoteResponse {
    return TvShowDetailsRemoteResponse(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        popularity = popularity,
        seasonCount = seasonCount,
        originCountry = originCountry,
        adult = adult,
        backdropPath = backdropPath,
        genres = genres,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        posterPath = posterPath,
        seasons = seasons,
        productionCompanies = productionCompanies,
        reviews = reviews,
        credits = credits,
        similar = similar,
        images = images,
        videos = videos
    )
}