package com.amsterdam.repository.utils

import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.dto.remote.RemoteTvShowItemDto

val  remoteMovieItemDto= RemoteMovieItemDto(
    id = 1,
    title = "Breaking Bad",
    overview = "A high school chemistry teacher diagnosed with cancer turns to making meth.",
    posterPath = "/somePath.jpg",
    voteAverage = 9.5,
    voteCount = 10000,
    genreIds = listOf(1, 2, 3),
    releaseDate = "2008-01-20",
    originalLanguage = "English",
    popularity = 100.0,
    backdropPath = "/someBackdropPath.jpg",
    adult = false,
    originalTitle = "Breaking Bad",
    video = false
)

val remoteTvShowItemDto= RemoteTvShowItemDto(
    id = 1,
    title = "Breaking Bad",
    overview = "A high school chemistry teacher diagnosed with cancer turns to making meth.",
    posterPath = "/somePath.jpg",
    voteAverage = 9.5,
    voteCount = 10000,
    genreIds = listOf(1, 2, 3),
    releaseDate = "2008-01-20",
    originalLanguage = "English",
    popularity = 100.0,
    backdropPath = "/someBackdropPath.jpg",
    originCountry = listOf("US"),
    adult = false,
    originalTitle = "Breaking Bad",
    seasons = emptyList(),
    seasonCount = 2,
    productionCompanies = emptyList(),
)