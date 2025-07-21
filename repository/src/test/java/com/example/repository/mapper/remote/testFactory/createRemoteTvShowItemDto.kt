package com.example.repository.mapper.remote.testFactory

import com.example.repository.dto.remote.RemoteTvShowItemDto

fun createRemoteTvShowItemDto(
    adult: Boolean = false,
    backdropPath: String? = "/backdrop.jpg",
    genreIds: List<Int> = listOf(10765, 18),
    id: Long = 101,
    originCountry: List<String> = listOf("US"),
    originalLanguage: String = "en",
    originalTitle: String = "Original Show",
    overview: String = "A test TV show",
    popularity: Double = 99.9,
    posterPath: String? = "/poster.jpg",
    releaseDate: String = "2022-08-15",
    title: String = "Test Show",
    voteAverage: Double = 8.3,
    voteCount: Int = 100
): RemoteTvShowItemDto {
    return RemoteTvShowItemDto(
        adult,
        backdropPath,
        genreIds,
        id,
        originCountry,
        originalLanguage,
        originalTitle,
        overview,
        popularity,
        posterPath,
        releaseDate,
        title,
        voteAverage,
        voteCount
    )
}