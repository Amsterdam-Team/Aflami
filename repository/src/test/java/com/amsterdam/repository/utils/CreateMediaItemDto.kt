package com.amsterdam.repository.utils

import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase
import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.repository.dto.remote.EpisodeRemoteDto
import com.amsterdam.repository.dto.remote.EpisodeRemoteResponse
import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.CastRemoteDto
import com.amsterdam.repository.dto.remote.MovieItemRemoteDto
import com.amsterdam.repository.dto.remote.MovieRemoteResponse
import com.amsterdam.repository.dto.remote.TvShowItemRemoteDto
import com.amsterdam.repository.dto.remote.TvShowRemoteResponse
import com.amsterdam.repository.dto.remote.SeasonRemoteDto
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.VideoRemoteDto
import com.amsterdam.repository.dto.remote.VideoRemoteResponse
import com.amsterdam.repository.dto.remote.movieGallery.GalleryRemoteResponse
import com.amsterdam.repository.dto.remote.review.ReviewsRemoteResponse
import kotlinx.datetime.LocalDate

val remoteMovieItemDto = MovieItemRemoteDto(
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

val remoteTvShowItemDto = TvShowItemRemoteDto(
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
val videoDto = VideoRemoteDto(
    id = "1",
    countryCode = "US",
    languageCode = "en",
    key = "someKey",
    name = "",
    official = true,
    publishedAt = "",
    site = "",
    size = 1080,
    type = "",
)
val remoteCastDto = CastRemoteDto(
    id = 1,
    name = "Actor One",
    character = "Char One",
    profilePath = "/img1.jpg",
    order = 1,
    adult = false,
    gender = 1,
    knownForDepartment = "Acting",
    popularity = 100.0,
    castId = 1,
    originalName = "Actor One",
    creditId = "creditId",
)
val tvShowDetailsRemoteResponse =
    TvShowDetailsRemoteResponse(
        id = 1,
        title = "Breaking Bad",
        overview = "A high school chemistry teacher diagnosed with cancer turns to making meth.",
        posterPath = "/somePath.jpg",
        voteAverage = 9.5,
        releaseDate = "2008-01-20",
        originalLanguage = "English",
        popularity = 100.0,
        backdropPath = "/someBackdropPath.jpg",
        adult = false,
        originalTitle = "Breaking Bad",
        seasons = listOf(
            SeasonRemoteDto(
                id = 1,
                title = "Season 1",
                episodeCount = 10,
                seasonNumber = 1,
                airDate = null
            ),
            SeasonRemoteDto(
                id = 2,
                title = "Season 2",
                episodeCount = 12,
                seasonNumber = 2,
                airDate = null
            )
        ),
        seasonCount = 2,
        productionCompanies = emptyList(),
        genres = emptyList(),
        originCountry = listOf("US"),
        reviews = ReviewsRemoteResponse(
            id = 1,
            page = 1,
            results = emptyList(),
            totalPages = 1,
            totalResults = 1,
        ),
        credits = CastAndCrewRemoteResponse(
            id = 1,
            cast = listOf(remoteCastDto),
            crew = emptyList(),
        ),
        similar = TvShowRemoteResponse(
            page = 1,
            results = listOf(remoteTvShowItemDto),
            totalPages = 1,
            totalResults = 1,
        ),
        images = GalleryRemoteResponse(
            id = 1,
            backdrops = emptyList(),
            posters = emptyList(),
            logos = emptyList()
        ),
        videos = VideoRemoteResponse(
            results = listOf(videoDto),
        ),
        accountStates = null,
    )
val remoteEpisodeDto = EpisodeRemoteDto(
    id = 1,
    title = "Breaking Bad",
    overview = "A high school chemistry teacher diagnosed with cancer turns to making meth.",
    voteAverage = 9.5,
    seasonNumber = 1,
    episodeNumber = 1,
    stillPath = "/somePath.jpg",
    runtime = "50",
    airDate = "2008-01-20",
)
val episodeResponse = EpisodeRemoteResponse(
    id = 1,
    overview = "A high school chemistry teacher diagnosed with cancer turns to making meth.",
    voteAverage = 9.5,
    seasonNumber = 1,
    airDate = "2008-01-20",
    episodes = listOf(remoteEpisodeDto),
    name = "Breaking Bad",
    posterPath = "/somePath.jpg",
)
val remoteMovieResponse = MovieRemoteResponse(
    page = 1,
    results = listOf(remoteMovieItemDto),
    totalPages = 1,
    totalResults = 1,

    )
val remoteCastAndCrewResponse = CastAndCrewRemoteResponse(
    id = 1,
    cast = listOf(remoteCastDto),
    crew = emptyList(),
)
val remoteUserRatedMovie = GetUserRatedMoviesUseCase.UserRatedMovie(
    movie = Movie(
        id = 1,
        name = "Breaking Bad",
        description = "A high school chemistry teacher diagnosed with cancer turns to making meth.",
        posterUrl = "https://image.tmdb.org/t/p/w500/somePath.jpg",
        releaseDate = LocalDate.parse("2008-01-20"),
        categories = listOf(MovieGenre.ALL, MovieGenre.ALL, MovieGenre.ALL),
        rating = 9.5f,
        popularity = 100.0,
        originCountry = "",
        runTimeInMinutes = 0,
        videoUrl = ""
    ),
    userRate = 0

)/*
val accountDetails = AccountDetailsDto(
    accountAvatar = AccountDetailsDto.AccountAvatar(
        gravatar = AccountDetailsDto.Gravatar(
            hash = "hash"
        ),
        movieDBData = AccountDetailsDto.MovieDBData(
            avatarPath = "avatarPath"
        )
    ),
    id = 1,
    includeAdult =true,
    countryIsoCode = "Eg",
    languageCode = "en",
    name = "Mona Ayman",
    username = "Mona",
)*/