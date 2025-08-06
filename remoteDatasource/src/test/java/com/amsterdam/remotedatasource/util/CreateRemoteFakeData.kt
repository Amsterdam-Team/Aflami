package com.amsterdam.remotedatasource.util

import com.amsterdam.repository.dto.remote.ActorSearchItemDto
import com.amsterdam.repository.dto.remote.EpisodeDto
import com.amsterdam.repository.dto.remote.EpisodeResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteCategoryDto
import com.amsterdam.repository.dto.remote.RemoteMovieDetailsResponse
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
import com.amsterdam.repository.dto.remote.RemoteTvShowItemDto
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.VideoDto
import com.amsterdam.repository.dto.remote.VideoResponse
import com.amsterdam.repository.dto.remote.authentication.AuthenticationResponseDto
import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.dto.remote.review.ReviewsResponse

val remoteMovieItemDto = RemoteMovieItemDto(
    id = 445566,
    title = "Upcoming Movie",
    adult = false,
    backdropPath = null,
    originalLanguage = "en",
    originalTitle = "Upcoming Movie",
    overview = "An overview...",
    popularity = 50.0,
    posterPath = null,
    releaseDate = "2025-01-01",
    video = false,
    voteAverage = 7.0,
    voteCount = 1000
)
val movieId = 550L

val remoteMovieDetailsResponse = RemoteMovieDetailsResponse(
    id = movieId,
    adult = false,
    backdropPath = null,
    originalLanguage = "en",
    originalTitle = "Test Movie",
    overview = "Overview",
    popularity = 1.0,
    posterPath = null,
    releaseDate = "2023-01-01",
    title = "Test Movie",
    video = false,
    voteAverage = 5.0,
    voteCount = 1,
    reviews = ReviewsResponse(
        id = movieId,
        page = 1,
        results = emptyList(),
        totalPages = 1,
        totalResults = 0
    ),
    credits = RemoteCastAndCrewResponse(
        id = movieId,
        cast = emptyList(),
        crew = emptyList()
    ),
    similar = RemoteMovieResponse(
        page = 1,
        results = emptyList(),
        totalPages = 1,
        totalResults = 0
    ),
    images = RemoteGalleryResponse(
        id = movieId,
        backdrops = emptyList(),
        logos = emptyList(),
        posters = emptyList()
    ),
    videos = VideoResponse(emptyList())
)
val name = "Leonardo DiCaprio"
val actorSearchItemDto=  ActorSearchItemDto(
    id = 6193,
    name = name,
    adult = false,
    gender = 2,
    knownFor = emptyList(),
    originalName = name,
    popularity = 70.0,
    profilePath = null
)

//tv
val remoteTvShowItemDto =  RemoteTvShowItemDto(
    id = 1L,
    title = "Popular Show",
    adult = false,
    backdropPath = null,
    genreIds = emptyList(),
    originCountry = emptyList(),
    originalLanguage = "en",
    originalTitle = "Popular Show",
    overview = "An overview of a popular show.",
    popularity = 500.0,
    posterPath = "/path/to/poster.jpg",
    releaseDate = "2024-01-01",
    voteAverage = 8.5,
    voteCount = 1000
)
val tvShowId = 123L
val remoteTvShowDetailsResponse = TvShowDetailsRemoteResponse(
    id = tvShowId,
    title = "Game of Thrones",
    overview = "Seven noble families...",
    backdropPath = "/backdrop_got.jpg",
    posterPath = "/poster_got.jpg",
    seasonCount = 8,
    adult = false,
    genres = listOf(RemoteCategoryDto(id = 10765, name = "Sci-Fi & Fantasy")),
    originCountry = listOf("US"),
    originalLanguage = "en",
    originalTitle = "Game of Thrones",
    popularity = 450.0,
    releaseDate = "2011-04-17",
    voteAverage = 8.4,
    productionCompanies = emptyList(),
    reviews = ReviewsResponse(
        id = tvShowId,
        page = 1,
        results = emptyList(),
        totalPages = 1,
        totalResults = 0
    ),
    credits = RemoteCastAndCrewResponse(
        id = tvShowId,
        cast = emptyList(),
        crew = emptyList()
    ),
    similar = RemoteTvShowResponse(
        page = 1,
        results = emptyList(),
        totalPages = 1,
        totalResults = 0
    ),
    images = RemoteGalleryResponse(
        id = tvShowId,
        backdrops = emptyList(),
        logos = emptyList(),
        posters = emptyList()
    ),
    videos = VideoResponse(emptyList())

)
val episodeDto=EpisodeDto(
    id = 456L,
    title = "Winter is Coming",
    seasonNumber = 1,
    episodeNumber = 1,
    overview = "An overview...",
    voteAverage = 8.5,
    runtime = "60",
    stillPath = "/still_path.jpg",
    airDate = "2011-04-17"
)
val episodeResponse=EpisodeResponse(
    id = 123L,
    episodes = listOf(episodeDto),
    airDate = "2011-04-17",
    name = "Season 1",
    overview = "Season 1 overview",
    posterPath = null,
    seasonNumber = 1L,
    voteAverage = 8.4
)
val videoDto= VideoDto(
    id = "123",
   languageCode = "en",
    countryCode = "US",
    key = "video_key",
    name = "Video Name",
    official = true,
    publishedAt = "2023-01-01",
    site = "YouTube",
    size = 1080,
    type = "Trailer"

)
val authenticationResponseDto= AuthenticationResponseDto(
    isSuccess =  true,
    expiresAt = "2023-01-01",
    requestToken = "token",
    statusCode = 200,
    statusMessage = "Success",
    error = ""
)


