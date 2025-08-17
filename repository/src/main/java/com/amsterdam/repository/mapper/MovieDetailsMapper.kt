package com.amsterdam.repository.mapper

import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase.MovieDetails
import com.amsterdam.repository.dto.local.MovieLocalDto
import com.amsterdam.repository.dto.remote.MovieDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.MovieItemRemoteDto
import com.amsterdam.repository.dto.remote.Rated
import com.amsterdam.repository.utils.toSafeLocalDate

fun MovieDetailsRemoteResponse.toEntity(): MovieDetails {
    val movieItemDto = MovieItemRemoteDto(
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
        movie = movieItemDto.toEntity(videoUrl = videos.results.firstOrNull()?.fullVideoUrl ?: ""),
        reviews = reviews.results.toEntityList(),
        actors = credits.cast.toEntityList(),
        similarMovies = similar.results.toMovieEntityList(isPoster = false),
        movieGallery = images.toEntityList(),
        moviePosters = images.toEntityList(),
        productionCompanies = productionCompanies.toEntityList(),
        userRate = if (accountStates?.rated is Rated.RatedValue) accountStates.rated.value.toInt() else null
    )
}

fun MovieDetailsRemoteResponse.toMovieItemDto(): MovieItemRemoteDto {
    return MovieItemRemoteDto(
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

fun MovieDetailsRemoteResponse.toLocalDto(storedLanguage: String): MovieLocalDto {
    return MovieLocalDto(
        movieId = id,
        storedLanguage = storedLanguage,
        name = title,
        description = overview,
        poster = posterPath.orEmpty(),
        releaseDate = releaseDate.toSafeLocalDate(),
        rating = voteAverage.toFloat(),
        popularity = popularity,
        movieLength = runtime,
        originCountry = originCountry.firstOrNull() ?: "",
        isAdult = adult,
    )
}