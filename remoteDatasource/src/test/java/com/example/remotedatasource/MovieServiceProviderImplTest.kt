package com.example.remotedatasource

import com.example.remotedatasource.api.MovieApiService
import com.example.remotedatasource.serviceProvider.implementation.MovieServiceProviderImpl
import com.example.repository.dto.remote.RemoteActorSearchResponse
import com.example.repository.dto.remote.RemoteCastAndCrewResponse
import com.example.repository.dto.remote.RemoteMovieItemDto
import com.example.repository.dto.remote.RemoteMovieResponse
import com.example.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.example.repository.dto.remote.review.ReviewsResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MovieServiceProviderImplTest {

    private lateinit var movieApiService: MovieApiService
    private lateinit var movieServiceProviderImpl: MovieServiceProviderImpl

    @Before
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
        movieServiceProviderImpl.getPopularMovies()

        // Then
        coVerify(exactly = 1) { movieApiService.getPopularMovies() }
    }

    @Test
    fun `getUpcomingMovies should call MovieApiService`() = runTest {
        // Given
        val dummyResponse = RemoteMovieResponse(
            page = 1, results = emptyList(), totalPages = 1, totalResults = 0
        )
        coEvery { movieApiService.getUpcomingMovies() } returns dummyResponse

        // When
        movieServiceProviderImpl.getUpcomingMovies()

        // Then
        coVerify(exactly = 1) { movieApiService.getUpcomingMovies() }
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
        movieServiceProviderImpl.getMoviesByKeyword(keyword, page)

        // Then
        coVerify(exactly = 1) { movieApiService.getMoviesByKeyword(keyword, page) }
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
        movieServiceProviderImpl.getActorIdByName(name, page)

        // Then
        coVerify(exactly = 1) { movieApiService.getActorIdByName(name, page) }
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
        movieServiceProviderImpl.getMoviesByActorId(actorIds)

        // Then
        coVerify(exactly = 1) { movieApiService.getMoviesByActorId(actorIds) }
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
        movieServiceProviderImpl.getMoviesByCountryIsoCode(countryIsoCode, page)

        // Then
        coVerify(exactly = 1) { movieApiService.getMoviesByCountryIsoCode(countryIsoCode, page) }
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
        movieServiceProviderImpl.getCastByMovieId(movieId)

        // Then
        coVerify(exactly = 1) { movieApiService.getCastByMovieId(movieId) }
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
        movieServiceProviderImpl.getMovieReviews(movieId)

        // Then
        coVerify(exactly = 1) { movieApiService.getMovieReviews(movieId) }
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
        movieServiceProviderImpl.getSimilarMovies(movieId)

        // Then
        coVerify(exactly = 1) { movieApiService.getSimilarMovies(movieId) }
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
        movieServiceProviderImpl.getMovieGallery(movieId)

        // Then
        coVerify(exactly = 1) { movieApiService.getMovieGallery(movieId) }
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
        movieServiceProviderImpl.getMoviePosters(movieId)

        // Then
        coVerify(exactly = 1) { movieApiService.getMoviePosters(movieId) }
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
        movieServiceProviderImpl.getMovieDetailsById(movieId)

        // Then
        coVerify(exactly = 1) { movieApiService.getMovieDetailsById(movieId) }
    }

    // --- getTopRatedMovies tests ---
    @Test
    fun `getTopRatedMovies should call MovieApiService`() = runTest {
        // Given
        val dummyResponse = RemoteMovieResponse(
            page = 1, results = emptyList(), totalPages = 1, totalResults = 0
        )
        coEvery { movieApiService.getTopRatedMovies() } returns dummyResponse

        // When
        movieServiceProviderImpl.getTopRatedMovies()

        // Then
        coVerify(exactly = 1) { movieApiService.getTopRatedMovies() }
    }
}