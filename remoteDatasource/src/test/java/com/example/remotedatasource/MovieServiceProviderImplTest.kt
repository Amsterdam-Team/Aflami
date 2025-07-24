package com.example.remotedatasource

import com.example.remotedatasource.api.MovieApiService
import com.example.remotedatasource.serviceProvider.implementation.MovieServiceProviderImpl
import com.example.repository.dto.remote.RemoteActorSearchResponse
import com.example.repository.dto.remote.RemoteCastAndCrewResponse
import com.example.repository.dto.remote.RemoteMovieItemDto
import com.example.repository.dto.remote.RemoteMovieResponse
import com.example.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.example.repository.dto.remote.review.ReviewsResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach

class MovieServiceProviderImplTest {

    private lateinit var movieApiService: MovieApiService
    private lateinit var movieServiceProviderImpl: MovieServiceProviderImpl

    @BeforeEach
    fun setUp() {
        movieApiService = mockk()
        movieServiceProviderImpl = MovieServiceProviderImpl(movieApiService)
    }

    @Test
    fun `getPopularMovies should call MovieApiService`() = runTest {
        // Given
        val dummyResponse = RemoteMovieResponse(
            page = 1, results = emptyList(), totalPages = 1, totalResults = 0
        )
        coEvery { movieApiService.getPopularMovies() } returns dummyResponse

        // When
        val result = movieServiceProviderImpl.getPopularMovies()

        // Then
        coVerify(exactly = 1) { movieApiService.getPopularMovies() }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getUpcomingMovies should call MovieApiService`() = runTest {
        // Given
        val dummyResponse = RemoteMovieResponse(
            page = 1, results = emptyList(), totalPages = 1, totalResults = 0
        )
        coEvery { movieApiService.getUpcomingMovies() } returns dummyResponse

        // When
        val result = movieServiceProviderImpl.getUpcomingMovies()

        // Then
        coVerify(exactly = 1) { movieApiService.getUpcomingMovies() }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getMoviesByKeyword should call MovieApiService`() = runTest {
        // Given
        val keyword = "action"
        val page = 1
        val dummyResponse = RemoteMovieResponse(
            page = 1, results = emptyList(), totalPages = 1, totalResults = 0
        )
        coEvery { movieApiService.getMoviesByKeyword(keyword, page) } returns dummyResponse

        // When
        val result = movieServiceProviderImpl.getMoviesByKeyword(keyword, page)

        // Then
        coVerify(exactly = 1) { movieApiService.getMoviesByKeyword(keyword, page) }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getActorIdByName should call MovieApiService`() = runTest {
        // Given
        val name = "actor"
        val page = 1
        val dummyResponse = RemoteActorSearchResponse(
            page = 1, actors = emptyList(), totalPages = 1, totalResults = 0
        )
        coEvery { movieApiService.getActorIdByName(name, page) } returns dummyResponse

        // When
        val result = movieServiceProviderImpl.getActorIdByName(name, page)

        // Then
        coVerify(exactly = 1) { movieApiService.getActorIdByName(name, page) }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getMoviesByActorId should call MovieApiService`() = runTest {
        // Given
        val actorIds = "123|456"
        val dummyResponse = RemoteMovieResponse(
            page = 1, results = emptyList(), totalPages = 1, totalResults = 0
        )
        coEvery { movieApiService.getMoviesByActorId(actorIds) } returns dummyResponse

        // When
        val result = movieServiceProviderImpl.getMoviesByActorId(actorIds)

        // Then
        coVerify(exactly = 1) { movieApiService.getMoviesByActorId(actorIds) }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getMoviesByCountryIsoCode should call MovieApiService`() = runTest {
        // Given
        val countryIsoCode = "US"
        val page = 1
        val dummyResponse = RemoteMovieResponse(
            page = 1, results = emptyList(), totalPages = 1, totalResults = 0
        )
        coEvery {
            movieApiService.getMoviesByCountryIsoCode(
                countryIsoCode,
                page
            )
        } returns dummyResponse

        // When
        val result = movieServiceProviderImpl.getMoviesByCountryIsoCode(countryIsoCode, page)

        // Then
        coVerify(exactly = 1) { movieApiService.getMoviesByCountryIsoCode(countryIsoCode, page) }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getCastByMovieId should call MovieApiService`() = runTest {
        // Given
        val movieId = 123L
        val dummyResponse = RemoteCastAndCrewResponse(
            id = movieId.toInt(), cast = emptyList(), crew = emptyList()
        )
        coEvery { movieApiService.getCastByMovieId(movieId) } returns dummyResponse

        // When
        val result = movieServiceProviderImpl.getCastByMovieId(movieId)

        // Then
        coVerify(exactly = 1) { movieApiService.getCastByMovieId(movieId) }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getMovieReviews should call MovieApiService`() = runTest {
        // Given
        val movieId = 123L
        val dummyResponse = ReviewsResponse(
            id = movieId, page = 1, results = emptyList(), totalPages = 1, totalResults = 0
        )
        coEvery { movieApiService.getMovieReviews(movieId) } returns dummyResponse

        // When
        val result = movieServiceProviderImpl.getMovieReviews(movieId)

        // Then
        coVerify(exactly = 1) { movieApiService.getMovieReviews(movieId) }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getSimilarMovies should call MovieApiService`() = runTest {
        // Given
        val movieId = 123L
        val dummyResponse = RemoteMovieResponse(
            page = 1, results = emptyList(), totalPages = 1, totalResults = 0
        )
        coEvery { movieApiService.getSimilarMovies(movieId) } returns dummyResponse

        // When
        val result = movieServiceProviderImpl.getSimilarMovies(movieId)

        // Then
        coVerify(exactly = 1) { movieApiService.getSimilarMovies(movieId) }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getMovieGallery should call MovieApiService`() = runTest {
        // Given
        val movieId = 123L
        val dummyResponse = RemoteGalleryResponse(
            id = movieId, backdrops = emptyList(), logos = emptyList(), posters = emptyList()
        )
        coEvery { movieApiService.getMovieGallery(movieId) } returns dummyResponse

        // When
        val result = movieServiceProviderImpl.getMovieGallery(movieId)

        // Then
        coVerify(exactly = 1) { movieApiService.getMovieGallery(movieId) }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getMoviePosters should call MovieApiService`() = runTest {
        // Given
        val movieId = 123L
        val dummyResponse = RemoteGalleryResponse(
            id = movieId, backdrops = emptyList(), logos = emptyList(), posters = emptyList()
        )
        coEvery { movieApiService.getMoviePosters(movieId) } returns dummyResponse

        // When
        val result = movieServiceProviderImpl.getMoviePosters(movieId)

        // Then
        coVerify(exactly = 1) { movieApiService.getMoviePosters(movieId) }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getMovieDetailsById should call MovieApiService`() = runTest {
        // Given
        val movieId = 123L
        val dummyResponse = RemoteMovieItemDto(
            adult = false,
            backdropPath = null,
            genres = emptyList(),
            id = movieId,
            originalLanguage = "",
            originalTitle = "",
            overview = "",
            popularity = 0.0,
            posterPath = null,
            releaseDate = "",
            runtime = 0,
            title = "",
            video = false,
            voteAverage = 0.0,
            voteCount = 0
        )
        coEvery { movieApiService.getMovieDetailsById(movieId) } returns dummyResponse

        // When
        val result = movieServiceProviderImpl.getMovieDetailsById(movieId)

        // Then
        coVerify(exactly = 1) { movieApiService.getMovieDetailsById(movieId) }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getTopRatedMovies should call MovieApiService`() = runTest {
        // Given
        val dummyResponse = RemoteMovieResponse(
            page = 1, results = emptyList(), totalPages = 1, totalResults = 0
        )
        coEvery { movieApiService.getTopRatedMovies() } returns dummyResponse

        // When
        val result = movieServiceProviderImpl.getTopRatedMovies()

        // Then
        coVerify(exactly = 1) { movieApiService.getTopRatedMovies() }
        assertThat(result).isEqualTo(dummyResponse)
    }
}