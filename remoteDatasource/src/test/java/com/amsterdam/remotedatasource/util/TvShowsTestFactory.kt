package com.amsterdam.remotedatasource.util

import com.amsterdam.repository.dto.remote.AccountStatesRemoteDto
import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.CategoryRemoteDto
import com.amsterdam.repository.dto.remote.CrewRemoteDto
import com.amsterdam.repository.dto.remote.EpisodeRemoteDto
import com.amsterdam.repository.dto.remote.EpisodeRemoteResponse
import com.amsterdam.repository.dto.remote.ProductionCompanyRemoteDto
import com.amsterdam.repository.dto.remote.RatingRemoteResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.TvShowItemRemoteDto
import com.amsterdam.repository.dto.remote.TvShowRemoteResponse
import com.amsterdam.repository.dto.remote.VideoRemoteDto
import com.amsterdam.repository.dto.remote.VideoRemoteResponse
import com.amsterdam.repository.dto.remote.movieGallery.GalleryImageRemoteDto
import com.amsterdam.repository.dto.remote.movieGallery.GalleryRemoteResponse
import com.amsterdam.repository.dto.remote.review.ReviewsRemoteResponse

const val TV_SHOW_TEST_ID = 1399L
const val TV_SHOW_TEST_KEYWORD = "Game of Thrones"
const val TV_SHOW_TEST_PAGE = 1
const val TV_SHOW_TEST_GENRE_ID = 10765L
const val TV_SHOW_TEST_SEASON_NUMBER = 1
const val TV_SHOW_TEST_EPISODE_NUMBER = 1
const val TV_SHOW_TEST_RATING = 8.5f

val tvShowItemRemoteDto = TvShowItemRemoteDto(
    adult = false,
    backdropPath = "/backdrop.jpg",
    genreIds = listOf(TV_SHOW_TEST_GENRE_ID.toInt()),
    id = TV_SHOW_TEST_ID,
    originCountry = listOf("US"),
    originalLanguage = "en",
    originalTitle = "Game of Thrones",
    overview = "Seven noble families fight for control of the mythical land of Westeros.",
    popularity = 120.0,
    posterPath = "/poster.jpg",
    releaseDate = "2011-04-17",
    title = "Game of Thrones",
    voteAverage = 8.4,
    voteCount = 20000,
    seasons = emptyList(),
    seasonCount = 8,
    productionCompanies = emptyList(),
    rating = 0f
)

val tvShowRemoteResponse = TvShowRemoteResponse(
    page = TV_SHOW_TEST_PAGE,
    results = listOf(tvShowItemRemoteDto),
    totalPages = 1,
    totalResults = 1
)

val tvShowDetailsRemoteResponse = TvShowDetailsRemoteResponse(
    adult = false,
    backdropPath = "/backdrop.jpg",
    genres = listOf(
        CategoryRemoteDto(
            id = TV_SHOW_TEST_GENRE_ID.toInt(),
            name = "Sci-Fi & Fantasy"
        )
    ),
    id = TV_SHOW_TEST_ID,
    originCountry = listOf("US"),
    originalLanguage = "en",
    originalTitle = "Game of Thrones",
    overview = "Seven noble families fight for control of the mythical land of Westeros.",
    popularity = 120.0,
    posterPath = "/poster.jpg",
    releaseDate = "2011-04-17",
    title = "Game of Thrones",
    voteAverage = 8.4,
    seasons = emptyList(),
    seasonCount = 8,
    productionCompanies = listOf(
        ProductionCompanyRemoteDto(
            id = 1,
            name = "HBO",
            logoPath = null,
            originCountry = "US"
        )
    ),
    reviews = ReviewsRemoteResponse(
        page = 1,
        results = emptyList(),
        totalPages = 1,
        totalResults = 0
    ),
    credits = CastAndCrewRemoteResponse(
        cast = emptyList(),
        crew = listOf(
            CrewRemoteDto(
                id = 1,
                name = "David Benioff",
                department = "Writing",
                job = "Creator",
                adult = false,
                gender = 2,
                knownForDepartment = "Writing",
                originalName = "David Benioff",
                popularity = 5.0,
                profilePath = "/profile.jpg",
                creditId = "credit123"
            )
        )
    ),
    similar = TvShowRemoteResponse(
        page = 1,
        results = emptyList(),
        totalPages = 0,
        totalResults = 0
    ),
    images = GalleryRemoteResponse(
        backdrops = listOf(
            GalleryImageRemoteDto(
                filePath = "/backdrop1.jpg",
                width = 1280,
                height = 720,
                aspectRatio = 1.778,
                voteAverage = 8.5,
                voteCount = 100
            )
        ), posters = emptyList(), logos = emptyList(), id = TV_SHOW_TEST_ID
    ),
    videos = VideoRemoteResponse(results = emptyList()),
    accountStates = AccountStatesRemoteDto()
)

val tvShowCastAndCrewRemoteResponse = CastAndCrewRemoteResponse(
    id = TV_SHOW_TEST_ID,
    cast = emptyList(),
    crew = emptyList()
)

val episodeRemoteResponse = EpisodeRemoteResponse(
    id = 12345,
    airDate = "2011-04-17",
    episodes = listOf(
        EpisodeRemoteDto(
            airDate = "2011-04-17",
            episodeNumber = TV_SHOW_TEST_EPISODE_NUMBER,
            id = 5451,
            overview = "Series premiere.",
            seasonNumber = TV_SHOW_TEST_SEASON_NUMBER,
            stillPath = "/still.jpg",
            voteAverage = 8.5,
            title = "Winter Is Coming",
            runtime = "62",
        )
    ),
    name = "Winter Is Coming",
    overview = "Series premiere.",
    posterPath = "/season_poster.jpg",
    seasonNumber = TV_SHOW_TEST_SEASON_NUMBER.toLong(),
    voteAverage = 8.5
)

val tvShowRatingRemoteResponse = RatingRemoteResponse(
    statusCode = 12,
    statusMessage = "The item was rated successfully."
)

val tvShowVideoRemoteResponse = VideoRemoteResponse(
    results = listOf(
        VideoRemoteDto(
            languageCode = "en",
            countryCode = "US",
            name = "Season 1 Trailer",
            key = "key123",
            site = "YouTube",
            size = 1080,
            type = "Trailer",
            official = true,
            publishedAt = "2011-03-01T00:00:00.000Z",
            id = "video1"
        )
    )
)