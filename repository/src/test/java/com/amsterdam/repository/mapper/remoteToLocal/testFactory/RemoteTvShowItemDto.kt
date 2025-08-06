package com.amsterdam.repository.mapper.remoteToLocal.testFactory

import com.amsterdam.repository.dto.remote.ProductionCompanyDto
import com.amsterdam.repository.dto.remote.RemoteTvShowItemDto
import com.amsterdam.repository.dto.remote.SeasonDto


fun createRemoteTvShowItemDto(
    id: Long = 1399L,
    title: String = "Game of Thrones",
    overview: String = "Seven noble families fight for control of the mythical land of Westeros.",
    posterPath: String? = "/poster.jpg",
    releaseDate: String = "2011-04-17",
    voteAverage: Double = 8.4,
    popularity: Double = 300.0,
    seasonCount: Int = 8,
    originCountry: List<String> = listOf("US"),
    backdropPath: String? = "/backdrop.jpg",
    originalLanguage: String = "en",
    originalTitle: String = "Game of Thrones",
    genreIds: List<Int> = emptyList(),
    adult: Boolean = false,
    voteCount: Int = 1000,
    seasons: List<SeasonDto> = emptyList(),
    productionCompanies: List<ProductionCompanyDto> = emptyList(),
    rating: Float = voteAverage.toFloat()
): RemoteTvShowItemDto {
    return RemoteTvShowItemDto(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        popularity = popularity,
        seasonCount = seasonCount,
        originCountry = originCountry,
        backdropPath = backdropPath,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        genreIds = genreIds,
        adult = adult,
        voteCount = voteCount,
        seasons = seasons,
        productionCompanies = productionCompanies,
        rating = rating
    )
}