package com.amsterdam.repository.mapper.remoteToLocal.testFactory

import com.amsterdam.repository.dto.remote.ProductionCompanyRemoteDto
import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.CategoryRemoteDto
import com.amsterdam.repository.dto.remote.TvShowRemoteResponse
import com.amsterdam.repository.dto.remote.SeasonRemoteDto
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.VideoRemoteResponse
import com.amsterdam.repository.dto.remote.movieGallery.GalleryRemoteResponse
import com.amsterdam.repository.dto.remote.review.ReviewsRemoteResponse

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
    genres: List<CategoryRemoteDto> = emptyList(),
    originalLanguage: String = "en",
    originalTitle: String = "Game of Thrones",
    seasons: List<SeasonRemoteDto> = emptyList(),
    productionCompanies: List<ProductionCompanyRemoteDto> = emptyList(),
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
    similar: TvShowRemoteResponse = TvShowRemoteResponse(
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