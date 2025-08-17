package com.amsterdam.repository.mapper

import com.amsterdam.repository.dto.local.MovieLocalDto
import com.amsterdam.repository.dto.remote.AccountStatesRemoteDto
import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.CategoryRemoteDto
import com.amsterdam.repository.dto.remote.MovieDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.MovieRemoteResponse
import com.amsterdam.repository.dto.remote.Rated
import com.amsterdam.repository.dto.remote.VideoRemoteDto
import com.amsterdam.repository.dto.remote.VideoRemoteResponse
import com.amsterdam.repository.dto.remote.movieGallery.GalleryRemoteResponse
import com.amsterdam.repository.dto.remote.review.ReviewsRemoteResponse
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class MovieDetailsMapperTest {
    @Test
    fun `toEntity should map response with null user rate when not rated`() {
        val remoteResponse = baseRemoteResponse.copy(
            accountStates = AccountStatesRemoteDto(rated = Rated.NotRated)
        )
        val result = remoteResponse.toEntity()

        assertThat(result.userRate).isNull()
        assertThat(result.movie.name).isEqualTo("Test Movie")
    }

    @Test
    fun `toEntity should map response with null user rate when accountStates is null`() {
        val remoteResponse = baseRemoteResponse.copy(accountStates = null)
        val result = remoteResponse.toEntity()

        assertThat(result.userRate).isNull()
    }

    @Test
    fun `toMovieItemDto should map response to MovieItemRemoteDto`() {
        val result = baseRemoteResponse.toMovieItemDto()

        assertThat(result.id).isEqualTo(101L)
        assertThat(result.title).isEqualTo("Test Movie")
        assertThat(result.runtime).isEqualTo(120)
        assertThat(result.genres).isEqualTo(baseRemoteResponse.genres)
    }

    @Test
    fun `toLocalDto should map response to MovieLocalDto`() {
        val result = baseRemoteResponse.toLocalDto("en")

        assertThat(result).isEqualTo(expectedMovieLocalDto)
    }

    @Test
    fun `toLocalDto should handle null poster and empty country list`() {
        val remoteResponseWithNulls = baseRemoteResponse.copy(
            posterPath = null,
            originCountry = emptyList()
        )
        val result = remoteResponseWithNulls.toLocalDto("ar")

        assertThat(result.poster).isEmpty()
        assertThat(result.originCountry).isEmpty()
    }

    private val baseRemoteResponse = MovieDetailsRemoteResponse(
        id = 101L,
        title = "Test Movie",
        overview = "An overview.",
        posterPath = "/poster.jpg",
        backdropPath = "/backdrop.jpg",
        releaseDate = "2023-01-01",
        voteAverage = 8.0,
        runtime = 120,
        originCountry = listOf("US"),
        videos = VideoRemoteResponse(
            results = listOf(
                VideoRemoteDto(
                    key = "12345", name = "Official Trailer", site = "YouTube", id = "videoId1",
                    languageCode = "en", countryCode = "US", size = 1080, type = "Trailer",
                    official = true, publishedAt = "2023-01-01T00:00:00.000Z"
                )
            )
        ),
        reviews = ReviewsRemoteResponse(
            id = 1L,
            page = 1,
            results = emptyList(),
            totalPages = 1,
            totalResults = 0
        ),
        similar = MovieRemoteResponse(
            page = 1,
            results = emptyList(),
            totalPages = 1,
            totalResults = 0
        ),
        credits = CastAndCrewRemoteResponse(cast = emptyList()),
        images = GalleryRemoteResponse(
            id = 1,
            backdrops = emptyList(),
            logos = emptyList(),
            posters = emptyList()
        ),
        productionCompanies = emptyList(),
        adult = false,
        originalLanguage = "en",
        originalTitle = "Test Movie Original",
        popularity = 100.0,
        video = false,
        voteCount = 500,
        genres = listOf(
            CategoryRemoteDto(
                id = 28,
                name = "Action"
            )
        )
    )

    private val expectedMovieLocalDto = MovieLocalDto(
        movieId = 101L,
        storedLanguage = "en",
        name = "Test Movie",
        description = "An overview.",
        poster = "/poster.jpg",
        releaseDate = LocalDate.parse("2023-01-01"),
        rating = 8.0f,
        popularity = 100.0,
        movieLength = 120,
        originCountry = "US",
        isAdult = false
    )
}