package com.example.repository.mapper.remote.testFactory

import com.example.repository.dto.remote.GenreDto
import com.example.repository.dto.remote.RemoteMovieItemDto

fun createRemoteMovieItemDto(
    id: Long = 1L,
    title: String = "Inception",
    overview: String = "A mind-bending thriller",
    genreIds: List<Int> = listOf(28, 12),
    genres: List<GenreDto> = emptyList(),
    releaseDate: String = "2010-07-16",
    voteAverage: Double = 8.8,
    popularity: Double = 1000.0,
    originCountry: List<String> = listOf("US"),
    runtime: Int = 148,
    posterPath: String? = "/poster.jpg",
    backdropPath: String? = "/backdrop.jpg",
    adult: Boolean = false,
    originalLanguage: String = "en",
    originalTitle: String = "Inception",
    video: Boolean = false,
    voteCount: Int = 20000
): RemoteMovieItemDto {
    return RemoteMovieItemDto(
        adult = adult,
        backdropPath = backdropPath,
        genreIds = genreIds,
        id = id,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        originCountry = originCountry,
        runtime = runtime,
        genres = genres
    )
}