package com.amsterdam.repository.mapper

import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.remote.*
import com.amsterdam.repository.dto.remote.movieGallery.GalleryRemoteResponse
import com.amsterdam.repository.dto.remote.review.ReviewsRemoteResponse
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TvShowDetailsMapperTest {
    @Test
    fun `toEntity should map response with user rate when rated`() {
        val remoteResponse = baseRemoteResponse.copy(
            accountStates = AccountStatesRemoteDto(rated = Rated.RatedValue(10.0f))
        )

        val result = remoteResponse.toEntity()

        assertThat(result.userRate).isEqualTo(10)
        assertThat(result.tvShow.name).isEqualTo("Test TV Show")
        assertThat(result.tvShow.videoUrl).isEqualTo("https://www.youtube.com/watch?v=67890")
    }

    @Test
    fun `toEntity should map response with null user rate when not rated`() {
        val remoteResponse = baseRemoteResponse.copy(
            accountStates = AccountStatesRemoteDto(rated = Rated.NotRated)
        )

        val result = remoteResponse.toEntity()

        assertThat(result.userRate).isNull()
    }

    @Test
    fun `toEntity should map response with null user rate when accountStates is null`() {
        val result = baseRemoteResponse.toEntity()

        assertThat(result.userRate).isNull()
    }

    @Test
    fun `toLocalDto should map response to TvShowLocalDto`() {
        val result = baseRemoteResponse.toLocalDto("en")

        assertThat(result).isEqualTo(expectedTvShowLocalDto)
    }

    private val baseRemoteResponse = TvShowDetailsRemoteResponse(
        id = 201L,
        title = "Test TV Show",
        overview = "A great TV show overview.",
        posterPath = "/tv_poster.jpg",
        backdropPath = "/tv_backdrop.jpg",
        releaseDate = "2022-01-01",
        voteAverage = 9.0,
        popularity = 200.0,
        seasonCount = 3,
        originCountry = listOf("CA"),
        videos = VideoRemoteResponse(
            results = listOf(
                VideoRemoteDto(
                    key = "67890", name = "Official TV Trailer", site = "YouTube", id = "tvVideoId1",
                    languageCode = "en", countryCode = "US", size = 1080, type = "Trailer",
                    official = true, publishedAt = "2022-01-01T00:00:00.000Z"
                )
            )
        ),
        genres = listOf(CategoryRemoteDto(id = 10759, name = "Action & Adventure")),
        credits = CastAndCrewRemoteResponse(cast = emptyList()),
        seasons = emptyList(),
        reviews = ReviewsRemoteResponse(
            id = 201L,
            page = 1,
            results = emptyList(),
            totalPages = 1,
            totalResults = 0
        ),
        similar = TvShowRemoteResponse(
            page = 1,
            results = emptyList(),
            totalPages = 1,
            totalResults = 0
        ),
        images = GalleryRemoteResponse(id = 2, backdrops = emptyList(), logos = emptyList(), posters = emptyList()),
        productionCompanies = emptyList(),
        accountStates = null,
        adult = false,
        originalLanguage = "en",
        originalTitle = "Test TV Show Original"
    )

    private val expectedTvShowLocalDto = TvShowLocalDto(
        tvShowId = 201L,
        storedLanguage = "en",
        name = "Test TV Show",
        description = "A great TV show overview.",
        poster = "https://image.tmdb.org/t/p/w500/tv_poster.jpg",
        airDate = LocalDate.parse("2022-01-01"),
        rating = 9.0f,
        popularity = 200.0,
        seasonCount = 3,
        originCountry = "CA"
    )
}