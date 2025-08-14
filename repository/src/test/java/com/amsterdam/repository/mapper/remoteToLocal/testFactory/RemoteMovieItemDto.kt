package com.amsterdam.repository.mapper.remoteToLocal.testFactory

import com.amsterdam.repository.dto.remote.ProductionCompanyRemoteDto
import com.amsterdam.repository.dto.remote.CategoryRemoteDto
import com.amsterdam.repository.dto.remote.MovieItemRemoteDto

fun createRemoteMovieItemDto(
    id: Long = 123L,
    title: String = "Example Movie",
    overview: String = "A simple overview.",
    posterPath: String? = "/poster.jpg",
    backdropPath: String? = "/backdrop.jpg",
    releaseDate: String = "2023-01-01",
    voteAverage: Double = 7.5,
    popularity: Double = 100.0,
    runtime: Int = 120,
    originCountry: List<String> = listOf("US"),
    adult: Boolean = false,
    genreIds: List<Int> = emptyList(),
    originalLanguage: String = "en",
    originalTitle: String = "Example Movie",
    productionCompanies: List<ProductionCompanyRemoteDto> = emptyList(),
    video: Boolean = false,
    voteCount: Int = 100,
    genres: List<CategoryRemoteDto> = emptyList(),
    rating: Float = voteAverage.toFloat()
): MovieItemRemoteDto {
    return MovieItemRemoteDto(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        popularity = popularity,
        runtime = runtime,
        originCountry = originCountry,
        adult = adult,
        genreIds = genreIds,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        productionCompanies = productionCompanies,
        video = video,
        voteCount = voteCount,
        genres = genres,
        rating = rating
    )
}