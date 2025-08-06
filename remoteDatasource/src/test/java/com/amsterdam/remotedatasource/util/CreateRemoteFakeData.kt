package com.amsterdam.remotedatasource.util

import com.amsterdam.repository.dto.remote.ActorSearchItemDto
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteMovieDetailsResponse
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
import com.amsterdam.repository.dto.remote.VideoResponse
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