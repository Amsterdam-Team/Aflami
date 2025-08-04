package com.amsterdam.repository.mapper.remote

import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase.MovieDetails
import com.amsterdam.repository.dto.remote.RemoteMovieDetailsResponse
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto

fun RemoteMovieDetailsResponse.toMovieDetailsEntity(): MovieDetails {
    val movieItemDto = RemoteMovieItemDto(
        adult = adult,
        backdropPath = backdropPath,
        genreIds = genreIds,
        id = id,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        productionCompanies = productionCompanies,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        originCountry = originCountry,
        runtime = runtime,
        genres = genres
    )

    return MovieDetails(
        movie = movieItemDto.toMovieEntity(),
        reviews = reviews.results.toReviewEntityList(),
        actors = credits.cast.toActorEntityList(),
        similarMovies = similar.results.toMovieEntityList(isPoster = false),
        movieGallery = images.toImageUrlsEntityList(),
        moviePosters = images.toImageUrlsEntityList()
    )
}

fun RemoteMovieDetailsResponse.toMovieItemDto(): RemoteMovieItemDto {
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
        productionCompanies = productionCompanies,
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