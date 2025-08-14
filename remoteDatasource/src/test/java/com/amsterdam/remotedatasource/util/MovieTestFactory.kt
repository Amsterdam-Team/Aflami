package com.amsterdam.remotedatasource.util

import com.amsterdam.repository.dto.remote.ActorSearchItemRemoteDto
import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.MovieDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.MovieItemRemoteDto
import com.amsterdam.repository.dto.remote.MovieRemoteResponse
import com.amsterdam.repository.dto.remote.VideoRemoteResponse
import com.amsterdam.repository.dto.remote.movieGallery.GalleryRemoteResponse
import com.amsterdam.repository.dto.remote.review.ReviewsRemoteResponse

const val MOVIE_TEST_KEYWORD = "Inception"
const val MOVIE_TEST_PAGE = 1
const val MOVIE_TEST_ACTOR_NAME = "Leonardo DiCaprio"
const val MOVIE_TEST_ACTOR_ID = 6193
const val MOVIE_TEST_ID = 550L
const val MOVIE_TEST_GENRE_ID = 28L
const val MOVIE_TEST_COUNTRY_ISO_CODE = "US"
const val MOVIE_TEST_RATING = 5.0f

val actorSearchItemRemoteDto = ActorSearchItemRemoteDto(
    id = MOVIE_TEST_ACTOR_ID,
    name = MOVIE_TEST_ACTOR_NAME,
    profilePath = "/test_profile_path.jpg",
    knownForDepartment = "Acting",
    originalName = MOVIE_TEST_ACTOR_NAME,
    popularity = 8.5,
    adult = false,
    gender = 2,
    knownFor = emptyList()
)

val movieItemRemoteDto = MovieItemRemoteDto(
    adult = false,
    backdropPath = "/backdrop.jpg",
    genreIds = listOf(28, 53),
    id = MOVIE_TEST_ID,
    originalLanguage = "en",
    originalTitle = "Fight Club",
    overview = "A ticking-time-bomb insomniac...",
    popularity = 50.0,
    posterPath = "/poster.jpg",
    releaseDate = "1999-10-15",
    title = "Fight Club",
    video = false,
    voteAverage = 8.4,
    voteCount = 1000,
    originCountry = listOf(MOVIE_TEST_COUNTRY_ISO_CODE)
)

val movieItemRemoteDtoWithNullDate = movieItemRemoteDto.copy(
    id = 551L,
    releaseDate = ""
)

val movieItemRemoteDtoWithNullPoster = movieItemRemoteDto.copy(
    id = 552L,
    posterPath = null
)

val movieDetailsRemoteResponse = MovieDetailsRemoteResponse(
    adult = false,
    backdropPath = "/backdrop.jpg",
    genreIds = emptyList(),
    id = MOVIE_TEST_ID,
    originalLanguage = "en",
    originalTitle = "Fight Club",
    overview = "A ticking-time-bomb insomniac...",
    popularity = 50.0,
    posterPath = "/poster.jpg",
    productionCompanies = emptyList(),
    releaseDate = "1999-10-15",
    title = "Fight Club",
    video = false,
    voteAverage = 8.4,
    voteCount = 1000,
    originCountry = emptyList(),
    runtime = 139,
    genres = emptyList(),
    reviews = ReviewsRemoteResponse(
        page = 1,
        results = emptyList(),
        totalPages = 1,
        totalResults = 0
    ),
    credits = CastAndCrewRemoteResponse(cast = emptyList(), crew = emptyList()),
    similar = MovieRemoteResponse(
        results = emptyList(),
        page = 1,
        totalPages = 1,
        totalResults = 0
    ),
    images = GalleryRemoteResponse(
        backdrops = emptyList(),
        posters = emptyList(),
        logos = emptyList(),
        id = MOVIE_TEST_ID
    ),
    videos = VideoRemoteResponse(results = emptyList()),
    accountStates = null
)